<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/konfig/konfig.jsp" %>
<%-- 
    Document   : supplierTambah
    Created on : Mar 14, 2015, 3:34:56 PM
    Author     : ade
--%>
<%@include file="/WEB-INF/layout/header.jsp" %>
<div class="row">
	<div class="col-xs-12">
		<h1>${headerapps}</h1>
	</div>
</div>

<div class="row">
	<div class="col-xs-12">
            <form class="form-horizontal" method="post">
                <div class="form-group" id="msg" style="background-color:yellow;text-align:center;display:none"></div>
			<div class="form-group">
				<label for="nomor_supplier" class="col-xs-2 control-label">Nomer Klaim Gransi</label>
				<div class="col-xs-10">
                                    <input type="text" class="form-control" name="no_klaim" id="no_klaim" placeholder="Nomer Klaim Gransi"<c:if test="${!empty dataEdit}"> value="${dataEdit.no_klaim}"</c:if>>
                                    <c:if test="${!empty dataEdit}"><input type="hidden" name="no_klaim1" value="${dataEdit.no_klaim}"></c:if>
				</div>
			</div>
			<div class="form-group">
				<label for="nama" class="col-xs-2 control-label">Tanggal</label>
				<div class="col-xs-10">
					<p><input type="text" class="form-control" id="tanggal" name="tanggal" style="width:130px" placeholder="Tanggal"<c:if test="${!empty dataEdit}"> value="${dataEdit.tanggal}"</c:if>></p>
				</div>
			</div>
			<div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">No. Faktur penjualan</label>
				<div class="col-xs-10">
                                    <textarea id="no_faktur" name="no_faktur" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.no_faktur}</c:if></textarea>
				</div>
			</div>
                        <div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">Pelanggan</label>
				<div class="col-xs-10">
                                    <textarea id="pelanggan" name="pelanggan" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.pelanggan}</c:if></textarea>
				</div>
			</div>
                        <div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">Kode Barang</label>
				<div class="col-xs-10">
                                    <textarea id="kode_barang" name="kode_barang" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.kode_barang}</c:if></textarea>
				</div>
			</div>
                        <div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">Jumlah</label>
				<div class="col-xs-10">
                                    <p><input type="text" class="form-control text-right numberfilter" id="jumlah" name="jumlah" style="width:130px" <c:if test="${!empty dataEdit}"> value="${dataEdit.jumlah}"</c:if><c:if test="${empty dataEdit}"> value="0"</c:if>></p>
				</div>
			</div>
			<c:if test="${empty isDetail}">
                        <div class="form-group">
				<div class="col-xs-offset-2 col-xs-10">
					<button type="submit" class="btn btn-success">Submit</button>
				</div>
			</div>
			</c:if>
		</form>
	</div>
</div>
<%@include file="/WEB-INF/layout/footer.jsp" %>
<script>
function makereadonly(elementtt) {
	elementtt.attr('readonly',true);
	elementtt.css('background-color','#fff');
	elementtt.css('border','none');
}
function addCommas( sValue ) 
{
    if (sValue.length > 0 ) {
    sValue = sValue.replace(/[#.]/g,'');
    sValue = sValue.replace(/[^0-9\.]/g,'');
    sValue1 = parseFloat(sValue);
    sValue = sValue1.toString();
        var sRegExp = new RegExp('(-?[0-9]+)([0-9]{3})');

        while(sRegExp.test(sValue)) {
            sValue = sValue.replace(sRegExp, '$1.$2');
        }
    }
    return sValue;
}
$( document ).ready(function() {
	<c:if test="${!empty isDetail}">
	
	makereadonly($('#tanggal'));
	makereadonly($('#no_faktur'));
	makereadonly($('#pelanggan'));
	makereadonly($('#kode_barang'));
	makereadonly($('#jumlah'));
	</c:if>
	makereadonly($('#no_klaim'));
	
	<c:if test="${empty isDetail}">
    $('.form-horizontal').submit(function(e) {
        e.preventDefault();
        var thisform = $(this);
        cansaved = true;
        msg = "";
        elemtt = $('#no_klaim');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Nomor Klaim Gransi";
            elemtt.focus();
        }
        elemtt = $('#tanggal');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Tanggal";
            elemtt.focus();
        }
        elemtt = $('#no_faktur');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi No. Faktur Penjualan";
            elemtt.focus();
        }
        
        elemtt = $('#pelanggan');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Pelanggan";
            elemtt.focus();
        }
        elemtt = $('#kode_barang');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Kode Barang";
            elemtt.focus();
        }
        elemtt = $('#jumlah');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Jumlah";
            elemtt.focus();
        }
        
        if (cansaved) {
            
            $.post('${baseURL}klaimgaransi/validation', thisform.serialize(),function(data){
                var jobj = data;
                if (jobj.cansaved == 1) {
                    thisform.unbind();
                    thisform.submit();
                } else {
                    $('#msg').text(jobj.msg);
                    $('#msg').css('display','block');
                }
            });
        } else {
            $('#msg').text(msg);
            $('#msg').css('display','block');
        }
    });
    
    $('#pelanggan')
        .textext({
            plugins : '<c:if test="${empty dataEdit}">prompt </c:if>autocomplete ajax',
            <c:if test="${empty dataEdit}">
            prompt  : 'Pelanggan',
            </c:if>
            ajax : {
                url : '${baseURL}pelanggan.json',
                dataType : 'json'                
            }
        });
    $('#kode_barang')
        .textext({
            plugins : '<c:if test="${empty dataEdit}">prompt </c:if>autocomplete ajax',
            <c:if test="${empty dataEdit}">
            prompt  : 'Kode Barang',
            </c:if>
            ajax : {
                url : '${baseURL}barang.json',
                dataType : 'json'                
            }
        });
    $('#no_faktur')
        .textext({
            plugins : '<c:if test="${empty dataEdit}">prompt </c:if>autocomplete ajax',
            <c:if test="${empty dataEdit}">
            prompt  : 'Nome Faktur Penjualan',
            </c:if>
            ajax : {
                url : '${baseURL}penjualan.json',
                dataType : 'json'                
            }
        });
        $( "#tanggal" ).datepicker({
     beforeShow: function(input, inst)
       {
          inst.dpDiv.css({marginTop: ($( "#tanggal" ).offset().top-190) + 'px', marginLeft: '0px'});
       },       
    dateFormat: 'dd/mm/yy'
    });
    jQuery('textarea').keydown(function(event) {
        if (event.keyCode == 13) {
            $('.form-horizontal').submit();
         }
    });
    
    $('body').on('keyup','.numberfilter',function(e) {
        $(this).val(addCommas($(this).val()));
    });
    </c:if>
    $('.numberfilter').each(function() {
        $(this).val(addCommas($(this).val()));
    });
});
</script>