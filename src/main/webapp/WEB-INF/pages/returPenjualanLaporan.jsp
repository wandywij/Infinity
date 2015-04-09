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
		<h1>Laporan Retur Penjualan</h1>
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
	<div class="col-xs-12">
		<table class="table">
			<colgroup>
				<col width="100" />
				<col width="100" />
				<col width="100" />
				<col width="100" />
				<col width="100" />
				<col width="100" />
			</colgroup>
			<thead>
				<tr>
                                        <th>Tanggal</th>
					<th>Nomer Retur Penjualan</th>					
					<th>No. Faktur Penjualan</th>
					<th>Pegawai</th>
					<th>Pelanggan</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
                            
                        <c:forEach items="${dataList}" var="data1">
                            <tr>
                            	<td><c:out value="${data1.tanggal}"/></td>
                                <td><a href="${baseURL}retur-penjualan/one-detail/${data1.no_retur_penjualan}">${data1.no_retur_penjualan}</a></td>
                                <td><a href="${baseURL}penjualan/laporan/detail/${data1.no_faktur}">${data1.no_faktur}</a></td>
                                <td><c:out value="${data1.pegawai}"/></td>
                                <td><c:out value="${data1.pelanggan}"/></td>
                            </tr>                    
                        </c:forEach>
			</tbody>
		</table>
	</div>
</div>
<%@include file="/WEB-INF/layout/footer.jsp" %>
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
        document.location = '${baseURL}penjualan/edit?kode='+kode;
    });
    $('.btdelete').click(function() {
        kode = $(this).data('kode');
        if (confirm('Apakah kode Nota Beli '+kode+' mau di hapus ?')) {
            document.location = '${baseURL}penjualan/delete?kode='+kode;
        }
    });
    $('.numberfilter').each(function() {
        $(this).text(addCommas($(this).text()));
    });
    $( "#to" ).datepicker({
        beforeShow: function(input, inst)
          {
             inst.dpDiv.css({marginTop: ($( "#to" ).offset().top+50) + 'px', marginLeft: '0px'});
          },       
       dateFormat: 'dd/mm/yy'
       });
    $( "#from" ).datepicker({
        beforeShow: function(input, inst)
          {
             inst.dpDiv.css({marginTop: ($( "#from" ).offset().top+50) + 'px', marginLeft: '0px'});
          },       
       dateFormat: 'dd/mm/yy'
       });
});
</script>