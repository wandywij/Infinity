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
                <label for="no_po" class="col-xs-2 control-label">Nomer PO</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" name="no_po" id="no_po" placeholder="Nomer PO"<c:if test="${!empty dataEdit}"> value="${dataEdit.no_po}"</c:if>>
                    <c:if test="${!empty dataEdit}"><input type="hidden" name="no_po1" value="${dataEdit.no_po}"></c:if>
                </div>
            </div>
            <div class="form-group">
                <label for="tanggal" class="col-xs-2 control-label">Tanggal</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" id="tanggal" name="tanggal" style="width:130px" placeholder="Tanggal"<c:if test="${!empty dataEdit}"> value="${dataEdit.tanggal}"</c:if>>
                </div>
            </div>
            <div class="form-group">
                <label for="supplier" class="col-xs-2 control-label">Supplier</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" id="supplier" name="supplier" placeholder="Supplier"<c:if test="${!empty dataEdit}"> value="${dataEdit.supplier}"</c:if>>
                </div>
            </div>
            <div class="form-group">
                <label for="pegawai" class="col-xs-2 control-label">Pegawai</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" id="pegawai" name="pegawai" placeholder="Pegawai"<c:if test="${!empty dataEdit}"> value="${dataEdit.pegawai}"</c:if>>
                </div>
            </div>
            <br/><br/>

            <div class="row">
                <div class="col-xs-12">
                    <table class="table">
                        <colgroup>
                            <col width="140" />
                            <col />
                            <col width="100" />
                            <c:if test="${empty isDetail}">
                            <col width="100" />
                            </c:if>
                        </colgroup>
                        <thead>
                            <tr>
                                <th>Kode Barang</th>
                                <th>Nama Barang</th>
                                <th style="text-align: right;">Jumlah</th>
                                <c:if test="${empty isDetail}">
                                <th></th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody id="tbodyitem">
                        <c:if test="${!empty dataEdit.detailData}">
                        <c:forEach items="${dataEdit.detailData}" var="data1">
                            <tr>
                                <td>${data1.kode_barang}<input type="hidden" name="kodebarang" value="${data1.kode_barang}"></td>
                                <td>${data1.nama_barang}<input type="hidden" name="namabarang" value="${data1.nama_barang}"></td>
                                <td><input type="text" class="form-control numberfilter" name="jumlah" style="text-align: right;" value="${data1.jumlah}"/></td>
                                <c:if test="${empty isDetail}">
                                <td><button type="button" class="btn btn-danger btdeleteitem">Delete</button></td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </c:if>
                        <c:if test="${empty isDetail}">
                            <tr>
                                <td><span id="kodebarang_text"></span><input type="hidden" id="kodebarang" name="kodebarang"></td>
                                <td><input type="text" class="form-control" id="namabarang" name="namabarang" placeholder="Barang"></td>
                                <td><input type="text" class="form-control numberfilter" name="jumlah" style="text-align: right;"/></td>
                                <c:if test="${empty isDetail}">
                                <td><button type="button" class="btn btn-danger btdeleteitem">Delete</button></td>
                                </c:if>
                            </tr>
                        </c:if>
                        </tbody>
                        <c:if test="${empty isDetail}">
                        <tfoot>
                            <tr>
                                <td class="text-right" colspan="${empty isDetail ? "4" : "3"}">
                                    <button type="button" id="btnTambahBarang" class="btn btn-success">Tambah Barang</button>
                                </td>
                            </tr>
                        </tfoot>
                        </c:if>
                    </table>
                </div>
            </div>
            <c:if test="${empty isDetail}">
            <br/>
            <div class="form-group text-right">
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
var cacheBarang = {};
function refreshautocompletebarang() {
    $('#namabarang').autocomplete({
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
        },
        select: function( event, ui ) {
            var tmp = ui.item.value;
            var tmp2 = tmp.indexOf(")", 1);
            var kode = tmp.substring(1, tmp2);
            var nama = tmp.substring(tmp2 + 2);
            $("#kodebarang_text").html(kode);
            $("#kodebarang").val(kode);
            $("#namabarang").val(nama);
            return false;
        }
    });
}
function addCommas( sValue ) {
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
    makereadonly($('#supplier'));
    makereadonly($('#pegawai'));
    makereadonly($('.numberfilter'));
    </c:if>

    makereadonly($('#no_po'));

    <c:if test="${empty isDetail}">
    $('.form-horizontal').submit(function(e) {
        e.preventDefault();
        var thisform = $(this);
        cansaved = true;
        msg = "";
        elemtt = $('#no_po');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Nomor PO";
            elemtt.focus();
        }
        elemtt = $('#tanggal');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Tanggal";
            elemtt.focus();
        }
        elemtt = $('#supplier');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Supplier";
            elemtt.focus();
        }
        elemtt = $('#pegawai');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Pegawai";
            elemtt.focus();
        }

        if (cansaved) {
            $.post('${baseURL}purchase-order/validation', thisform.serialize(),function(data){
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
    $('body').on('keydown','.numberfilter',function(e) {
    if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
            (e.keyCode == 65 && e.ctrlKey === true) ||
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 return;
        }
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    });

    $('body').on('keyup','.numberfilter',function(e) {
        $(this).val(addCommas($(this).val()));
    });

    $( "#tanggal" ).datepicker({
        beforeShow: function(input, inst) {
            inst.dpDiv.css({marginTop: ($( "#tanggal" ).offset().top-190) + 'px', marginLeft: '0px'});
        },
        dateFormat: 'dd/mm/yy'
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
    $('#btnTambahBarang').click(function(e) {
        elmenttvall = $('#tbodyitem').children().last().children().eq(0).find('input[name="kodebarang"]').val();
        $('#tbodyitem').children().last().children().eq(0).html(elmenttvall+'<input type="hidden" name="kodebarang" value="'+elmenttvall+'">');
        elmenttvall = $('#tbodyitem').children().last().children().eq(1).find('input[name="namabarang"]').val();
        $('#tbodyitem').children().last().children().eq(1).html(elmenttvall+'<input type="hidden" name="namabarang" value="'+elmenttvall+'">');

        komponen = '<tr>'
             +'<td><span id="kodebarang_text"></span><input type="hidden" id="kodebarang" name="kodebarang"></td>'
             +'<td><input type="text" class="form-control" id="namabarang" name="namabarang" placeholder="Barang"></td>'
             +'<td><input type="text" class="form-control numberfilter" name="jumlah" style="text-align: right;"/></td>'
             +'<td><button type="button" class="btn btn-danger btdeleteitem">Delete</button></td>'
             +'</tr>';

        $('#tbodyitem').append(komponen);
        refreshautocompletebarang();
    });
    $('body').on('click','.btdeleteitem',function(){
        $(this).parent().parent().remove();
    });

    refreshautocompletebarang();
    </c:if>
    <c:if test="${!empty dataEdit}">
    $('.numberfilter').each(function() {
        $(this).val(addCommas($(this).val()));
    });
</c:if>
});
</script>
<%@include file="/WEB-INF/layout/footer.jsp" %>
