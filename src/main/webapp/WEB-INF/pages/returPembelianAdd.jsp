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
				<label for="nomor_supplier" class="col-xs-2 control-label">Nomer Retur Pembelian</label>
				<div class="col-xs-10">
                                    <input type="text" class="form-control" name="no_retur_pembelian" id="no_retur_pembelian" placeholder="Nomer Retur Pembelian"<c:if test="${!empty dataEdit}"> value="${dataEdit.no_retur_pembelian}"</c:if>>
                                    <c:if test="${!empty dataEdit}"><input type="hidden" name="no_retur_pembelian1" value="${dataEdit.no_retur_pembelian}"></c:if>
				</div>
			</div>
			<div class="form-group">
				<label for="nama" class="col-xs-2 control-label">Tanggal</label>
				<div class="col-xs-10">
					<p><input type="text" class="form-control" id="tanggal" name="tanggal" style="width:130px" placeholder="Tanggal"<c:if test="${!empty dataEdit}"> value="${dataEdit.tanggal}"</c:if>></p>
				</div>
			</div>
			<div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">No. Purchase Order</label>
				<div class="col-xs-10">
                                    <textarea id="no_po" name="no_po" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.no_po}</c:if></textarea>
				</div>
			</div>
                        <div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">Pegawai</label>
				<div class="col-xs-10">
                                    <textarea id="pegawai" name="pegawai" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.pegawai}</c:if></textarea>
				</div>
			</div>
                        <div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">Supplier</label>
				<div class="col-xs-10">
                                    <textarea id="supplier" name="supplier" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.supplier}</c:if></textarea>
				</div>
			</div>
                        <div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">Kode Barang</label>
				<div class="col-xs-10">
                                    <textarea id="kode_barang" name="kode_barang" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.kode_barang}</c:if></textarea>
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
	makereadonly($('#tanggal'));
	makereadonly($('#no_po'));
	makereadonly($('#pegawai'));
	makereadonly($('#supplier'));
	makereadonly($('#kode_barang'));
</c:if>
makereadonly($('#no_retur_pembelian'));

<c:if test="${empty isDetail}">
    $('.form-horizontal').submit(function(e) {
        e.preventDefault();
        var thisform = $(this);
        cansaved = true;
        msg = "";
        elemtt = $('#no_retur_pembelian');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Nomor Retur Pembelian";
            elemtt.focus();
        }
        elemtt = $('#tanggal');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Tanggal";
            elemtt.focus();
        }
        elemtt = $('#no_po');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi No. Purchase Order";
            elemtt.focus();
        }
        elemtt = $('#pegawai');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Pegawai";
            elemtt.focus();
        }
        elemtt = $('#supplier');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Suppplier";
            elemtt.focus();
        }
        elemtt = $('#kode_barang');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Kode Barang";
            elemtt.focus();
        }
        
        if (cansaved) {
            
            $.post('${baseURL}retur-pembelian/validation', thisform.serialize(),function(data){
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
    
    $('#supplier')
        .textext({
            plugins : '<c:if test="${empty dataEdit}">prompt </c:if>autocomplete ajax',
            <c:if test="${empty dataEdit}">
            prompt  : 'Supplier',
            </c:if>
            ajax : {
                url : '${baseURL}supplier.json',
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
            }
        });
    $('#no_po')
        .textext({
            plugins : '<c:if test="${empty dataEdit}">prompt </c:if>autocomplete ajax',
            <c:if test="${empty dataEdit}">
            prompt  : 'Kode Purchase Order',
            </c:if>
            ajax : {
                url : '${baseURL}po.json',
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
    </c:if>
});
</script>