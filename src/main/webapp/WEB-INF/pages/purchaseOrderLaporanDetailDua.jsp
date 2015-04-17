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
        <h1>${headerapps} ${dataEdit.no_po}</h1>
    </div>
</div>

<div class="row">
    <div class="col-xs-12">
        <form class="form-horizontal" method="post">
<!--            <div class="form-group" id="msg" style="background-color:yellow;text-align:center;display:none"></div>
            <div class="form-group">
                <label for="no_po" class="col-xs-2 control-label">Nomer PO</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" name="no_po" id="no_po" placeholder="Nomer PO"<c:if test="${!empty dataEdit}"> value="${dataEdit.no_po}"</c:if>>
                    <c:if test="${!empty dataEdit}"><input type="hidden" name="no_po1" value="${dataEdit.no_po}"></c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label for="nama" class="col-xs-2 control-label">Tanggal</label>
                    <div class="col-xs-10">
                        <p><input type="text" class="form-control" id="tanggal" name="tanggal" style="width:130px" placeholder="Tanggal"<c:if test="${!empty dataEdit}"> value="${dataEdit.tanggal}"</c:if>></p>
                    </div>
                </div>
                <div class="form-group">
                    <label for="alamat" class="col-xs-2 control-label">Supplier</label>
                    <div class="col-xs-10">
                        <textarea id="supplier" name="supplier" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.supplier}</c:if></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label for="alamat" class="col-xs-2 control-label">Pegawai</label>
                    <div class="col-xs-10">
                        <textarea id="pegawai" name="pegawai" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.pegawai}</c:if></textarea>
                    </div>
                </div>-->
                <br/><br/>

                <div class="row">
                    <div class="col-xs-12">
                        <table class="table">
                            <colgroup>
                                <col width="10%" />
                                <col width="15%" />
                                <col width="25%" />
                                <col width="25%" />
                                <col width="25%" />
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>Kode Barang</th>
                                    <th>Nama Barang</th>
                                    <th style="text-align: right;">Jumlah</th>
                                    <th style="text-align: right">Harga</th>
                                    <th style="text-align: right">Total</th>

                                </tr>
                            </thead>
                            <tbody id="tbodyitem">                              
                            <c:forEach items="${dataEdit.detailData}" var="data1">
                                <tr>
                                    <td>${data1.kode_barang}<input type="hidden" name="kodebarang" value="${data1.kode_barang}"></td>
                                    <td>${data1.nama_barang}<input type="hidden" name="namabarang" value="${data1.nama_barang}"></td>      
                                    <td style="text-align: right;">${data1.jumlah}</td>
<!--                                    <td><label class="numberfilter" id="lblHarga" name="lblHarga">${data1.harga}</label></td>
                                    <td><label class="numberfilter" id="lblHarga" name="lblHarga">${data1.total}</label></td>-->
                                    <td class="numberfilter text-right">
                                        <c:out value="${data1.harga}"/>
                                    </td>
                                    <td class="numberfilter text-right">
                                        <c:out value="${data1.total}"/>
                                    </td>
                                </tr>                         
                            </c:forEach>
                            <tr>
                                <td colspan="4" class="text-right">Total</td>
                                <td class="numberfilter text-right"><c:out value="${dataEdit.grandTotal}"/></td>
                            </tr>
                        </tbody>                       
                    </table>

                </div>
            </div>

        </form>
    </div>
</div>
<script>
    function addCommas(sValue)
    {
        if (sValue.length > 0) {
            sValue = sValue.replace(/[#.]/g, '');
            sValue = sValue.replace(/[^0-9\.]/g, '');
            sValue1 = parseFloat(sValue);
            sValue = sValue1.toString();
            var sRegExp = new RegExp('(-?[0-9]+)([0-9]{3})');

            while (sRegExp.test(sValue)) {
                sValue = sValue.replace(sRegExp, '$1.$2');
            }
        }
        return "Rp. " + sValue;
    }
    $(document).ready(function () {
        $('.btedit').click(function () {
            kode = $(this).data('kode');
            document.location = '${baseURL}purchase-order/edit?kode=' + kode;
        });
        $('.btdelete').click(function () {
            kode = $(this).data('kode');
            if (confirm('Apakah kode Nota Beli ' + kode + ' mau di hapus ?')) {
                document.location = '${baseURL}purchase-order/delete?kode=' + kode;
            }
        });
        $('.numberfilter').each(function () {
            $(this).text(addCommas($(this).text()));
        });
        $("#to").datepicker({
            beforeShow: function (input, inst)
            {
                inst.dpDiv.css({marginTop: ($("#to").offset().top - 70) + 'px', marginLeft: '0px'});
            },
            dateFormat: 'dd/mm/yy'
        });
        $("#from").datepicker({
            beforeShow: function (input, inst)
            {
                inst.dpDiv.css({marginTop: ($("#from").offset().top - 70) + 'px', marginLeft: '0px'});
            },
            dateFormat: 'dd/mm/yy'
        });
    });
</script>
<%@include file="/WEB-INF/layout/footer.jsp" %>
