<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/konfig/konfig.jsp" %>
<%-- 
    Document   : index
    Created on : Mar 14, 2015, 2:23:50 PM
    Author     : ade
--%>
<%@include file="/WEB-INF/layout/header.jsp" %>
<!DOCTYPE html>

<div class="row">
    <div class="col-xs-6">
        <h1>Stok Detail</h1>
    </div>
    <div class="col-xs-6 text-right">
        <form class="form-inline">
            <label for="from">From</label>
            <input type="text" class="form-control" id="from" name="from" 
                   <c:if test="${!empty startDate }">value="<c:out value="${startDate}"/>"</c:if>>
                   <label for="to">to</label>
                   <input type="text" class="form-control" id="to" name="to"
                   <c:if test="${!empty endDate }">value="<c:out value="${endDate}"/>"</c:if>>

                   <button type="submit" class="btn btn-success">Go</button>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12 text-left">
            <table class="table1">
                <tr>
                    <td>Kode Barang</td>
                    <td>:</td>
                    <td>${barang.kode_barang}</td>
            </tr>
            <tr>
                <td>Nama Barang</td>
                <td>:</td>
                <td>${barang.nama_barang}</td>
            </tr>
        </table>
    </div>
</div>

<div class="row">
    <br/><br/><br/>
    <div class="col-xs-12">
        <table class="table">
            <colgroup>
                <col width="200" />
                <col />
                <col width="100" />
            </colgroup>
            <thead>
                <tr>
                    <th>Kode Transaksi</th>
                    <th>Tanggal</th>
                    <th class="text-right">Jumlah</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${trxList}" var="data1">
                    <tr <c:if test="${data1.warna==0 }"> class="danger"</c:if>>
                        <td><c:out value="${data1.kode_transaksi}"/></td>
                        <td><c:out value="${data1.tanggal}"/></td>
                        <td class="text-right numberfilter"><c:out value="${data1.jumlah}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@include file="/WEB-INF/layout/footer.jsp" %>
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
        return sValue;
    }
    $(document).ready(function () {
        $('.btedit').click(function () {
            kode = $(this).data('kode');
            document.location = '${baseURL}barang/edit?kode=' + kode;
        });
        $('.btdelete').click(function () {
            kode = $(this).data('kode');
            if (confirm('Apakah kode barang ' + kode + ' mau di hapus ?')) {
                document.location = '${baseURL}barang/delete?kode=' + kode;
            }
        });
        $('.numberfilter').each(function () {
            $(this).text(addCommas($(this).text()));
        });
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
</script>