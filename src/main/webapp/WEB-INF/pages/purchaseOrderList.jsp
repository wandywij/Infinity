<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/konfig/konfig.jsp" %>
<%-- 
    Document   : index
    Created on : Mar 14, 2015, 2:23:50 PM
    Author     : ade
--%>
<%@include file="/WEB-INF/layout/header.jsp" %>
<div class="row">
	<div class="col-xs-8">
		<h1>Nota Beli</h1>
	</div>
	<div class="col-xs-4 text-right">
		<a href="${baseURL}purchase-order/add" class="btn btn-success">Tambah Baru</a>
	</div>
</div>

<div class="row">
	<div class="col-xs-12">
		<table class="table">
			<colgroup>
				<col width="100" />
				<col width="100" />
				<col width="100" />
				<col width="100" />
				<col width="100" />
			</colgroup>
			<thead>
				<tr>
					<th>Nomer PO</th>
					<th>Tanggal</th>
					<th>Supplier</th>
					<th>Pegawai Entry</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
                            
                        <c:forEach items="${dataList}" var="data1">
                            <tr>
                                <td><a href="${baseURL}purchase-order/one-detail/<c:out value="${data1.no_po}"/>"><c:out value="${data1.no_po}"/></a></td>
                                <td><c:out value="${data1.tanggal}"/></td>
                                <td><c:out value="${data1.supplier}"/></td>
                                <td><c:out value="${data1.pegawai}"/></td>
                                <td>
                                    <button type="button" class="btn btn-warning btedit" data-kode="${data1.no_po}">Edit</button>
                                    <button type="button" class="btn btn-danger btdelete" data-kode="${data1.no_po}">Delete</button>
                                </td>
                            </tr>                    
                        </c:forEach>
			</tbody>
		</table>
	</div>
</div>
<script>
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
    return "Rp. " + sValue;
}
$( document ).ready(function() {
    $('.btedit').click(function() {
        kode = $(this).data('kode');
        document.location = '${baseURL}purchase-order/edit?kode='+kode;
    });
    $('.btdelete').click(function() {
        kode = $(this).data('kode');
        if (confirm('Apakah kode Nota Beli '+kode+' mau di hapus ?')) {
            document.location = '${baseURL}purchase-order/delete?kode='+kode;
        }
    });
    $('.numberfilter').each(function() {
        $(this).text(addCommas($(this).text()));
    });
});
</script>
<%@include file="/WEB-INF/layout/footer.jsp" %>
