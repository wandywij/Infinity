<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/konfig/konfig.jsp" %>
<%-- 
    Document   : pelangganTambah
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
				<label for="nomor_pelanggan" class="col-xs-2 control-label">Nomor Pelanggan</label>
				<div class="col-xs-10">
                                    <input type="text" class="form-control" name="nomor_pelanggan" id="nomor_pelanggan" placeholder="Nomor Pelanggan"<c:if test="${!empty dataEdit}"> value="${dataEdit.kode_pelanggan}"</c:if>>
                                    <c:if test="${!empty dataEdit}"><input type="hidden" name="nomor_pelanggan1" value="${dataEdit.kode_pelanggan}"></c:if>
				</div>
			</div>
			<div class="form-group">
				<label for="nama" class="col-xs-2 control-label">Nama</label>
				<div class="col-xs-10">
					<input type="text" class="form-control" id="nama" name="nama" placeholder="Nama"<c:if test="${!empty dataEdit}"> value="${dataEdit.nama_pelanggan}"</c:if>>
				</div>
			</div>
			<div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">Alamat</label>
				<div class="col-xs-10">
					<input type="text" class="form-control" id="alamat" name="alamat" placeholder="Alamat"<c:if test="${!empty dataEdit}"> value="${dataEdit.alamat_pelanggan}"</c:if>>
				</div>
			</div>
			<div class="form-group">
				<label for="telp" class="col-xs-2 control-label">Telp</label>
				<div class="col-xs-10">
					<input type="text" class="form-control" id="telp" name="telp" placeholder="Telp"<c:if test="${!empty dataEdit}"> value="${dataEdit.telepon_pelanggan}"</c:if>>
				</div>
			</div>
			<div class="form-group">
				<label for="email" class="col-xs-2 control-label">Email</label>
				<div class="col-xs-10">
					<input type="email" class="form-control" id="email" name="email" placeholder="Email"<c:if test="${!empty dataEdit}"> value="${dataEdit.email_pelanggan}"</c:if>>
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
$( document ).ready(function() {
	<c:if test="${!empty isDetail}">
	makereadonly($('#nama'));
	makereadonly($('#alamat'));
	makereadonly($('#telp'));
	makereadonly($('#email'));
</c:if>
makereadonly($('#nomor_pelanggan'));
    $('.form-horizontal').submit(function(e) {
        e.preventDefault();
        var thisform = $(this);
        cansaved = true;
        msg = "";
        elemtt = $('#nomor_pelanggan');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Nomor pelanggan";
            elemtt.focus();
        }
        elemtt = $('#nama');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Nama pelanggan";
            elemtt.focus();
        }
        elemtt = $('#alamat');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Alamat pelanggan";
            elemtt.focus();
        }
        elemtt = $('#telp');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Telepon pelanggan";
            elemtt.focus();
        }
        
        if (cansaved) {
            
            $.post('${baseURL}pelanggan/validation', thisform.serialize(),function(data){
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
});
</script>