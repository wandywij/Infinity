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
                <label for="no_retur_pembelian" class="col-xs-2 control-label">Nomer Retur Pembelian</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" name="no_retur_pembelian" id="no_retur_pembelian" placeholder="Nomer Retur Pembelian"<c:if test="${!empty dataEdit}"> value="${dataEdit.no_retur_pembelian}"</c:if>>
                    <c:if test="${!empty dataEdit}"><input type="hidden" name="no_retur_pembelian1" value="${dataEdit.no_retur_pembelian}"></c:if>
                </div>
            </div>
            <div class="form-group">
                <label for="tanggal" class="col-xs-2 control-label">Tanggal</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" id="tanggal" name="tanggal" style="width:130px" placeholder="Tanggal"<c:if test="${!empty dataEdit}"> value="${dataEdit.tanggal}"</c:if>>
                </div>
            </div>
            <div class="form-group">
                <label for="no_po" class="col-xs-2 control-label">No. Purchase Order</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" id="no_po" name="no_po" placeholder="No. Purchase Order"<c:if test="${!empty dataEdit}"> value="${dataEdit.no_po}"</c:if>>
                </div>
            </div>
            <div class="form-group">
                <label for="pegawai" class="col-xs-2 control-label">Pegawai</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" id="pegawai" name="pegawai" placeholder="Pegawai"<c:if test="${!empty dataEdit}"> value="${dataEdit.pegawai}"</c:if>>
                </div>
            </div>
            <div class="form-group">
                <label for="supplier" class="col-xs-2 control-label">Supplier</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" id="supplier" name="supplier" placeholder="Supplier"<c:if test="${!empty dataEdit}"> value="${dataEdit.supplier}"</c:if>>
                </div>
            </div>
            <div class="form-group">
                <label for="kode_barang" class="col-xs-2 control-label">Barang</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" name="kode_barang" id="kode_barang" placeholder="Barang"<c:if test="${!empty dataEdit}"> value="${dataEdit.kode_barang}"</c:if>>
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

    var cacheSupplier = {};
    $('#supplier').autocomplete({
        minLength: 0,
        source: function( request, response ) {
            var term = request.term;
            if (term[0] == "(" && term.indexOf(")", 1) != -1)
                request.term = term = term.substring(1, term.indexOf(")", 1));
            if ( term in cacheSupplier ) {
                response( cacheSupplier[ term ] );
                return;
            }

            $.getJSON( "${baseURL}supplier.json", request, function( data, status, xhr ) {
                cacheSupplier[ term ] = data;
                response( data );
            });
        }
    });
    var cachePegawai = {};
    $('#pegawai').autocomplete({
        minLength: 0,
        source: function( request, response ) {
            var term = request.term;
            if (term[0] == "(" && term.indexOf(")", 1) != -1)
                request.term = term = term.substring(1, term.indexOf(")", 1));
            if ( term in cachePegawai ) {
                response( cachePegawai[ term ] );
                return;
            }

            $.getJSON( "${baseURL}pegawai.json", request, function( data, status, xhr ) {
                cachePegawai[ term ] = data;
                response( data );
            });
        }
    });
    var cacheBarang = {};
    $('#kode_barang').autocomplete({
        minLength: 0,
        source: function( request, response ) {
            var term = request.term;
            if (term[0] == "(" && term.indexOf(")", 1) != -1)
                request.term = term = term.substring(1, term.indexOf(")", 1));
            if ( term in cacheBarang ) {
                response( cacheBarang[ term ] );
                return;
            }

            $.getJSON( "${baseURL}barang.json", request, function( data, status, xhr ) {
                cacheBarang[ term ] = data;
                response( data );
            });
        }
    });
    var cachePo = {};
    $('#no_po').autocomplete({
        minLength: 0,
        source: function( request, response ) {
            var term = request.term;
            if (term[0] == "(" && term.indexOf(")", 1) != -1)
                request.term = term = term.substring(1, term.indexOf(")", 1));
            if ( term in cachePo ) {
                response( cachePo[ term ] );
                return;
            }

            $.getJSON( "${baseURL}po.json", request, function( data, status, xhr ) {
                cachePo[ term ] = data;
                response( data );
            });
        }
    });
    $( "#tanggal" ).datepicker({
        beforeShow: function(input, inst) {
            inst.dpDiv.css({marginTop: ($( "#tanggal" ).offset().top-190) + 'px', marginLeft: '0px'});
        },
        dateFormat: 'dd/mm/yy'
    });
    </c:if>
});
</script>
<%@include file="/WEB-INF/layout/footer.jsp" %>
