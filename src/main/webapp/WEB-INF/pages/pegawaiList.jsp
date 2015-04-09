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
	<div class="col-xs-8">
		<h1>Pegawai</h1>
	</div>
	<div class="col-xs-4 text-right">
		<a href="${baseURL}pegawai/add" class="btn btn-success">Tambah Baru</a>
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
                                <col width="100" />
			</colgroup>
			<thead>
				<tr>
					<th>ID Pegawai</th>
					<th>Nama</th>
                                        <th>Divisi</th>
					<th>Alamat</th>
					<th>Telp</th>
					<th>Email</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
                            
                        <c:forEach items="${dataList}" var="data1">
                            <tr>
                                <td><a href="${baseURL}pegawai/one-detail/<c:out value="${data1.id_pegawai}"/>"><c:out value="${data1.id_pegawai}"/></a></td>
                                <td><c:out value="${data1.nama_pegawai}"/></td>
                                <td><c:out value="${data1.divisi}"/></td>
                                <td><c:out value="${data1.alamat_pegawai}"/></td>
                                <td><c:out value="${data1.telepon_pegawai}"/></td>
                                <td><c:out value="${data1.email_pegawai}"/></td>
                                <td>
                                    <button type="button" class="btn btn-warning btedit" data-kode="${data1.id_pegawai}">Edit</button>
                                    <button type="button" class="btn btn-danger btdelete" data-kode="${data1.id_pegawai}">Delete</button>
                                </td>
                            </tr>                    
                        </c:forEach>
			</tbody>
		</table>
	</div>
</div>
<%@include file="/WEB-INF/layout/footer.jsp" %>
<script>
$( document ).ready(function() {
    $('.btedit').click(function() {
        kode = $(this).data('kode');
        document.location = '${baseURL}pegawai/edit?kode='+kode;
    });
    $('.btdelete').click(function() {
        kode = $(this).data('kode');
        if (confirm('Apakah ID pegawai '+kode+' mau di hapus ?')) {
            document.location = '${baseURL}pegawai/delete?kode='+kode;
        }
    });
});
</script>