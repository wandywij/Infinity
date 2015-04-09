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
				<label for="nomor_supplier" class="col-xs-2 control-label">Nomer Retur Penjualan</label>
				<div class="col-xs-10">
                                    <input type="text" class="form-control" name="no_returpenjualan" id="no_returpenjualan" placeholder="Nomer Retur Penjualan"<c:if test="${!empty dataEdit}"> value="${dataEdit.no_returpenjualan}"</c:if>>
                                    <c:if test="${!empty dataEdit}"><input type="hidden" name="no_returpenjualan1" value="${dataEdit.no_returpenjualan}"></c:if>
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
				<label for="alamat" class="col-xs-2 control-label">Pegawai</label>
				<div class="col-xs-10">
                                    <textarea id="pegawai" name="pegawai" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.pegawai}</c:if></textarea>
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
				<label for="alamat" class="col-xs-2 control-label">Nama Barang</label>
				<div class="col-xs-10">
                                    <input type="text" class="form-control" name="nama_barang" id="nama_barang" placeholder="Nama Barang"<c:if test="${!empty dataEdit}"> value="${dataEdit.nama_barang}"</c:if>>
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
	makereadonly($('#pegawai'));
	makereadonly($('#pelanggan'));
	makereadonly($('#kode_barang'));
	makereadonly($('#nama_barang'));
	var elamaentt = $('#jumlah');
	makereadonly(elamaentt);
	elamaentt.val(addCommas(elamaentt.val()));
</c:if>	

makereadonly($('#no_returpenjualan'));

<c:if test="${empty isDetail}">
    $('.form-horizontal').submit(function(e) {
        e.preventDefault();
        var thisform = $(this);
        cansaved = true;
        msg = "";
        elemtt = $('#no_returpenjualan');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Nomor Retur Penjualan";
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
        elemtt = $('#pegawai');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Pegawai";
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
            
            $.post('${baseURL}retur-penjualan/validation', thisform.serialize(),function(data){
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
    $('#pegawai')
        .textext({
            plugins : '<c:if test="${empty dataEdit}">prompt </c:if>autocomplete ajax',
            <c:if test="${empty dataEdit}">
            prompt  : 'Pegawai',
            </c:if>
            ajax : {
                url : '${baseURL}pegawai.json',
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
            },
ext: {
    core: {
        trigger: function()
        {
            
            if (arguments[0]=='setInputData') {
                if (arguments[1].length>0) {
                    if (arguments[1].substr(0,1) == '(') {
                        strdata = arguments[1].substr(1);
                        posisitutupkurung = 0;
                        lengthstr = strdata.length;
                        i = 0;
                        ketemu = false;
                        while (!ketemu && i<lengthstr) {
                            str1kata = strdata.substr(i,1);
                            if (str1kata == ')') {
                                ketemu = true;
                            } else {
                                i++;
                            }
                        }
                        $('#nama_barang').val(strdata.substr(i+2));
                    }
                }
            }
            $.fn.textext.TextExt.prototype.trigger.apply(this, arguments);
        }
    }
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
    $('.numberfilter').each(function() {
        $(this).val(addCommas($(this).val()));
    });
    $('body').on('keyup','.numberfilter',function(e) {
        $(this).val(addCommas($(this).val()));
    });
    </c:if>
});
</script>