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
		<h1>Laporan Pembelian <c:out value="${kode}"/></h1>
	</div>
</div>

<div class="row">
	<div class="col-xs-12">
		<table class="table">
			<colgroup>
				<col width="100" />
				<col width="150" />
				<col width="150" />
				<col width="100" />
				<col width="100" />
			</colgroup>
			<thead>
				<tr>
					<th>Tanggal</th>
					<th>Kode Barang</th>
					<th>Nama Barang</th>
					<th style="text-align: right;">Jumlah</th>
					<th style="text-align: right;">Total</th>
				</tr>
			</thead>
			<tbody>
                        <c:forEach items="${dataList}" var="data1">
                            <tr>
                                <td><c:out value="${tanggal}"/></td>
                                <td><c:out value="${data1.kodeBarang}"/></td>
                                <td><c:out value="${data1.namaBarang}"/></td>
                                <td class="numberfilter text-right"><c:out value="${data1.jumlah}"/></td>
                                <td class="numberfilter text-right"><c:out value="${data1.total}"/></td>
                            </tr>                    
                        </c:forEach>
                        <tr>
                                <td colspan="6" class="text-right">Total</td>
                                <td class="numberfilter text-right"><c:out value="${total}"/></td>
                            </tr>
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
    return sValue;
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
    $( "#to" ).datepicker({
        beforeShow: function(input, inst)
          {
             inst.dpDiv.css({marginTop: ($( "#to" ).offset().top-70) + 'px', marginLeft: '0px'});
          },       
       dateFormat: 'dd/mm/yy'
       });
    $( "#from" ).datepicker({
        beforeShow: function(input, inst)
          {
             inst.dpDiv.css({marginTop: ($( "#from" ).offset().top-70) + 'px', marginLeft: '0px'});
          },       
       dateFormat: 'dd/mm/yy'
       });
});
</script>