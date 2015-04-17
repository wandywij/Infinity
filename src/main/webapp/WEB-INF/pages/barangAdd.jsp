<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/konfig/konfig.jsp" %>
<%-- 
    Document   : barangTambah
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
				<label for="kode_barang" class="col-xs-2 control-label">Kode barang</label>
				<div class="col-xs-10">
                                    <input type="text" class="form-control" name="kode_barang" id="kode_barang" placeholder="Kode barang"<c:if test="${!empty dataEdit}"> value="${dataEdit.kode_barang}"</c:if>>
                                    <c:if test="${!empty dataEdit}"><input type="hidden" name="kode_barang1" value="${dataEdit.kode_barang}"></c:if>
				</div>
			</div>
			<div class="form-group">
				<label for="nama" class="col-xs-2 control-label">Nama</label>
				<div class="col-xs-10">
					<input type="text" class="form-control" id="nama" name="nama" placeholder="Nama"<c:if test="${!empty dataEdit}"> value="${dataEdit.nama_barang}"</c:if>>
				</div>
			</div>
			<div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">Harga</label>
				<div class="col-xs-10">
					<input type="text" class="form-control numberfilter" style="text-align: right" id="harga" name="harga" placeholder="Harga"<c:if test="${!empty dataEdit}"> value="${dataEdit.harga}"</c:if>>
				</div>
			</div>
<!--			<div class="form-group">
				<label for="telp" class="col-xs-2 control-label">Jumlah Stok</label>
				<div class="col-xs-10">
					<input type="text" class="form-control numberfilter"  style="text-align: right" id="jumlah_stok" name="jumlah_stok" placeholder="jumlah_stok"<c:if test="${!empty dataEdit}"> value="${dataEdit.jumlah_stok}"</c:if>>
				</div>
			</div>-->
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

<script>
function makereadonly(elementtt) {
	elementtt.attr('readonly',true);
	elementtt.css('background-color','#fff');
	elementtt.css('border','none');
}
$( document ).ready(function() {
	
	<c:if test="${!empty isDetail}">
		makereadonly($('#nama'));
		makereadonly($('#harga'));
		makereadonly($('#jumlah_stok'));
	</c:if>
	makereadonly($('#kode_barang'));
    $('.form-horizontal').submit(function(e) {
        e.preventDefault();
        var thisform = $(this);
        cansaved = true;
        msg = "";
        elemtt = $('#kode_barang');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Nomor barang";
            elemtt.focus();
        }
        elemtt = $('#nama');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Nama barang";
            elemtt.focus();
        }
        
        if (cansaved) {
            
            $.post('${baseURL}barang/validation', thisform.serialize(),function(data){
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
    $('.numberfilter').keydown(function(e) {
    if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
            (e.keyCode == 65 && e.ctrlKey === true) || 
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 return;
        }
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    }).keyup(function(event) {
        $(this).val(addCommas($(this).val()));
    }); 
    $('.numberfilter').each(function() {
        $(this).val(addCommas($(this).val()));
    });
});
</script>
<%@include file="/WEB-INF/layout/footer.jsp" %>
