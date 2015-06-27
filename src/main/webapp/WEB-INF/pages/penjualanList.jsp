<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/konfig/konfig.jsp" %>
<%-- 
    Document   : index
    Created on : Mar 14, 2015, 2:23:50 PM
    Author     : ade
--%>
<%@include file="/WEB-INF/layout/header.jsp" %>
<div class="row">
    <table class="table">
        <tr>
            <td><h1>Penjualan</h1></td>
            <td class="pull-right">
                <a href="${baseURL}penjualan/add" class="btn btn-success">Tambah Baru</a>
            </td>
            <td class="pull-right">
                <input type="text" class="form-control" id="search_penjualan" name="search_penjualan" 
                   style="text-align: right;" placeholder="Nomor Faktur" />
            </td>
        </tr>
    </table>
<!--	<div class="col-xs-8">
		<h1>Penjualan</h1>
	</div>
	<div class="col-xs-4 pull-right">
            <input type="text" class="form-control" id="search_penjualan" name="search_penjualan" 
                   style="text-align: right;" placeholder="Nomor Faktur" />
             <a href="${baseURL}penjualan/add" class="btn btn-success">Tambah Baru</a>
	</div>-->
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
					<th>Nomer Faktur</th>
					<th>Tanggal</th>
					<th>Pelanggan</th>
					<th>Pegawai Entry</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
                            
                        <c:forEach items="${dataList}" var="data1">
                            <tr>
                                <td><a href="${baseURL}penjualan/one-detail/<c:out value="${data1.no_faktur}"/>"><c:out value="${data1.no_faktur}"/></a></td>
                                <td><c:out value="${data1.tanggal}"/></td>
                                <td><c:out value="${data1.pelanggan}"/></td>
                                <td><c:out value="${data1.pegawai}"/></td>
                                <td>
                                    <button type="button" class="btn btn-warning btedit" data-kode="${data1.no_faktur}">Edit</button>
                                    <button type="button" class="btn btn-danger btdelete" data-kode="${data1.no_faktur}">Delete</button>
                                </td>
                            </tr>                    
                        </c:forEach>
			</tbody>
		</table>
	</div>
</div>
<script>
function refreshAutoCompletePenjualan() {
    var cacheBarang = {};
    $('#search_penjualan').autocomplete({
        minLength: 0;
        source: function (request, response) {
            var term = request.term;
            if (term[0] == "(" && term.indexOf(")", 1) != -1) 
                request.term = term = term.substring(1, term.indexOf(")", 1));
            if (term in cacheBarang) {
                response(cacheBarang[term]);
                return;
            }
            $.getJSON("${baseURL}penjualan2.json", request, function (data, status, xhr) {
                    cacheBarang[term] = data;
                    response(data);
                });
        },
        select: function (event, ui) {
            var tmp = ui.item.value;
            var tmp2 = tmp.indexOf("/ ", 1);
            var nama = tmp.substring(1, tmp2);
            var kode = tmp.substring(tmp2 + 2);
//                $("#kodebarang_text").html(kode);
//                $("#kodebarang").val(kode);
//                $("#namabarang").val(nama);
            //$('#content').replaceWith()'${baseURL}karyawan/' + kode);
            $('#content').load('${baseURL}penjualan/' + kode);            
            return false;
        }
     });
}
refreshAutoCompletePenjualan();

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
});
</script>
<%@include file="/WEB-INF/layout/footer.jsp" %>
