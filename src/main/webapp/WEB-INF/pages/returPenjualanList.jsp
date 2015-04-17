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
		<h1>Retur Penjualan</h1>
	</div>
	<div class="col-xs-4 text-right">
		<a href="${baseURL}retur-penjualan/add" class="btn btn-success">Tambah Baru</a>
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
				<col width="100" />
<!--                                <col width="100" />
                                <col width="100" />-->
			</colgroup>
			<thead>
				<tr>
					<th>Nomer Retur Penjualan</th>
					<th>Tanggal</th>
					<th>No. Faktur Penjualan</th>
					<th>Pegawai</th>
					<th>Pelanggan</th>
<!--                                        <th>Barang</th>
                                        <th>Jumlah</th>-->
					<th></th>
				</tr>
			</thead>
			<tbody>
                            
                        <c:forEach items="${dataList}" var="data1">
                            <tr>
                                <td><a href="${baseURL}retur-penjualan/one-detail/<c:out value="${data1.no_retur_penjualan}"/>"><c:out value="${data1.no_retur_penjualan}"/></a></td>
                                <td><c:out value="${data1.tanggal}"/></td>
                                <td><c:out value="${data1.no_faktur}"/></td>
                                <td><c:out value="${data1.pegawai}"/></td>
                                <td><c:out value="${data1.pelanggan}"/></td>
                                <!--<td><c:out value="${data1.barang}"/></td>-->
<!--                                <td class=""numberfilter><c:out value="${data1.jumlah}"/></td>-->
                                <td>
                                    <button type="button" class="btn btn-warning btedit" data-kode="${data1.no_retur_penjualan}">Edit</button>
                                    <button type="button" class="btn btn-danger btdelete" data-kode="${data1.no_retur_penjualan}">Delete</button>
                                </td>
                            </tr>                    
                        </c:forEach>
			</tbody>
		</table>
	</div>
</div>
<script>
$( document ).ready(function() {
    $('.btedit').click(function() {
        kode = $(this).data('kode');
        document.location = '${baseURL}retur-penjualan/edit?kode='+kode;
    });
    $('.btdelete').click(function() {
        kode = $(this).data('kode');
        if (confirm('Apakah kode Retur Penjualan '+kode+' mau di hapus ?')) {
            document.location = '${baseURL}retur-penjualan/delete?kode='+kode;
        }
    });
});
</script>
<%@include file="/WEB-INF/layout/footer.jsp" %>
