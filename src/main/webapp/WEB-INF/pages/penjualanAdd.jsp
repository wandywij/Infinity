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
				<label for="no_faktur" class="col-xs-2 control-label">Nomer Faktur</label>
				<div class="col-xs-10">
                                    <input type="text" class="form-control" name="no_faktur" id="no_faktur" placeholder="Nomer Faktur"<c:if test="${!empty dataEdit}"> value="${dataEdit.no_faktur}"</c:if>>
                                    <c:if test="${!empty dataEdit}"><input type="hidden" name="no_faktur1" value="${dataEdit.no_faktur}"></c:if>
				</div>
			</div>
			<div class="form-group">
				<label for="nama" class="col-xs-2 control-label">Tanggal</label>
				<div class="col-xs-10">
					<p><input type="text" class="form-control" id="tanggal" name="tanggal" style="width:130px" placeholder="Tanggal"<c:if test="${!empty dataEdit}"> value="${dataEdit.tanggal}"</c:if>></p>
				</div>
			</div>
			<div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">Pelanggan</label>
				<div class="col-xs-10">
                                    <textarea id="pelanggan" name="pelanggan" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.pelanggan}</c:if></textarea>
				</div>
			</div>
                        <div class="form-group">
				<label for="alamat" class="col-xs-2 control-label">Pegawai</label>
				<div class="col-xs-10">
                                    <textarea id="pegawai" name="pegawai" rows="1"><c:if test="${!empty dataEdit}">${dataEdit.pegawai}</c:if></textarea>
				</div>
			</div>
<br/><br/>
                                
 <div class="row">
	<div class="col-xs-12">
		<table class="table">
			<colgroup>
				<col width="210" />
				<col width="220" />
				<col width="70" />
				<col width="40" />
				<col width="40" />
				<col width="40" /><c:if test="${empty isDetail}">
                                <col width="40" />
                                </c:if>
			</colgroup>
			<thead>
				<tr>
					<th>Kode Barang</th>
					<th>Nama Barang</th>
					<th style="text-align: right;">Jumlah</th>
					<th style="text-align: right;">Harga</th>
					<th style="text-align: right;">Diskon (%)</th>
					<th style="text-align: right;">Total</th>
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
                                    <td><input type="text" class="form-control numberfilter" name="harga" style="text-align: right;" value="${data1.harga}"/></td>
                                    <td><input type="text" class="form-control numberfilter" name="diskon" style="text-align: right;" value="${data1.diskon}"/></td>
                                    <td><input type="text" class="form-control numberfilter" name="total" style="text-align: right;" value="${data1.total}"/></td>
                                    <c:if test="${empty isDetail}">
                                    <td><button type="button" class="btn btn-danger btdeleteitem">Delete</button></td>
                                    </c:if>
                                </tr>                         
                            </c:forEach>
                            </c:if>
                            <c:if test="${empty isDetail}">
				<tr>
					<td><textarea id="kodebarang" name="kodebarang" rows="1"></textarea></td>
					<td><textarea id="namabarang" name="namabarang" rows="1"></textarea></td>
					<td><input type="text" class="form-control numberfilter jumlah" name="jumlah" style="text-align: right;"/></td>
                                        <td><input type="text" class="form-control numberfilter harga" name="harga" style="text-align: right;"/></td>
                                        <td><input type="text" class="form-control numberfilter diskon" name="diskon" value="0" style="text-align: right;"/></td>
                                        <td><input type="text" class="form-control numberfilter total" name="total" value="0" style="text-align: right;"/></td>
					<td><button type="button" class="btn btn-danger btdeleteitem">Delete</button></td>
				</tr>
				
				</c:if>
			</tbody>
			 <c:if test="${empty isDetail}">
			<tfoot>
						<tr>
							<td class="text-right" colspan="7">
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
<%@include file="/WEB-INF/layout/footer.jsp" %>
<script>
function makereadonly(elementtt) {
	elementtt.attr('readonly',true);
	elementtt.css('background-color','#fff');
	elementtt.css('border','none');
}
function refreshautocompletebarang(isbeginner) {    
    $('#kodebarang')
        .textext({
            plugins : 'prompt autocomplete ajax',
            prompt  : 'Kode Barang',
            ajax : {
                url : '${baseURL}barang.json',
                dataType : 'json'                
            },
ext: {
    core: {
        trigger: function()
        {
            
            if (arguments[0]=='setInputData') {
                if (arguments[1].length>0) {
                    if (arguments[1].substr(0,1) == '(') {
                        strdata = arguments[1].substr(1);
                        posisitutupkurung = 0;
                        lengthstr = strdata.length;
                        i = 0;
                        ketemu = false;
                        while (!ketemu && i<lengthstr) {
                            str1kata = strdata.substr(i,1);
                            if (str1kata == ')') {
                                ketemu = true;
                            } else {
                                i++;
                            }
                        }
                        kodee = strdata.substr(0,i);
                        
                        parentelement = this.input().parent().parent().parent().parent().parent().children().last();
                        parentelement.children().children().eq(1).find('textarea#namabarang').val(strdata.substr(i+2));
                        parentelement.children().children().eq(1).find('.text-prompt').remove();
                        parentelement.children().children().eq(1).find('input[name="namabarang"]').val(strdata.substr(i+2));
                                               
                        parentelement.children().children().eq(0).find('input[name="kodebarang"]').val(kodee);
                        
                    }
                }
            }
            if (arguments[0]=='hideDropdown') {
                parentelement = this.input().parent().parent().parent().parent().parent().children().last();
                valueeee = parentelement.children().children().eq(0).find('input[name="kodebarang"]').val();
                if (valueeee.length>0) {
                    parentelement.children().children().eq(0).find('textarea#kodebarang').val(valueeee);
                }
            }
            $.fn.textext.TextExt.prototype.trigger.apply(this, arguments);
        }
    }
}
        })
        ;
   $('#namabarang')
        .textext({
            plugins : 'prompt autocomplete ajax',
            prompt  : 'Nama Barang',
            ajax : {
                url : '${baseURL}barang.json',
                dataType : 'json'                
            },
ext: {
    core: {
        trigger: function()
        {
            
            if (arguments[0]=='setInputData') {
                if (arguments[1].length>0) {
                    if (arguments[1].substr(0,1) == '(') {
                        strdata = arguments[1].substr(1);
                        posisitutupkurung = 0;
                        lengthstr = strdata.length;
                        i = 0;
                        ketemu = false;
                        while (!ketemu && i<lengthstr) {
                            str1kata = strdata.substr(i,1);
                            if (str1kata == ')') {
                                ketemu = true;
                            } else {
                                i++;
                            }
                        }
                        kodee = strdata.substr(0,i);
                        
                        parentelement = this.input().parent().parent().parent().parent().parent().children().last();                        
                                                
                        parentelement.children().children().eq(1).find('input[name="namabarang"]').val(strdata.substr(i+2));
                                               
                        parentelement.children().children().eq(0).find('input[name="kodebarang"]').val(kodee);
                        parentelement.children().children().eq(0).find('textarea#kodebarang').val(kodee);
                        parentelement.children().children().eq(0).find('.text-prompt').remove();
                        
                    }
                }
            }
            if (arguments[0]=='hideDropdown') {
                parentelement = this.input().parent().parent().parent().parent().parent().children().last();
                valueeee = parentelement.children().children().eq(1).find('input[name="namabarang"]').val();
                if (valueeee.length>0) {
                    parentelement.children().children().eq(1).find('textarea#namabarang').val(valueeee);
                }
            }
            $.fn.textext.TextExt.prototype.trigger.apply(this, arguments);
        }
    }
}
        })
        ;
}

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

function countdata() {
    $('table .jumlah').each(function() {
           var jumlah = $(this).parent().parent().children().eq(2).children().val().replace(/\./g,'');
           var harga = $(this).parent().parent().children().eq(3).children().val().replace(/\./g,'');
           var diskon = $(this).parent().parent().children().eq(4).children().val().replace(/\./g,'');
           var hargasetelahdiskon = harga - (harga*diskon/100);
           var total = jumlah * hargasetelahdiskon;
           total = addCommas(total.toString());
           var totalelement = $(this).parent().parent().children().eq(5).children();           
           totalelement.val(total);
    });
}
$( document ).ready(function() {
	<c:if test="${!empty isDetail}">
	makereadonly($('#tanggal'));
	makereadonly($('#pelanggan'));
	makereadonly($('#pegawai'));
	makereadonly($('.numberfilter'));
	</c:if>
	
	makereadonly($('#no_faktur'));
	
	<c:if test="${empty isDetail}">
    $('.form-horizontal').submit(function(e) {
        e.preventDefault();
        var thisform = $(this);
        cansaved = true;
        msg = "";
        elemtt = $('#no_faktur');
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
        elemtt = $('#pelanggan');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi pelanggan";
            elemtt.focus();
        }
        elemtt = $('#pegawai');
        if (cansaved && elemtt.val().length < 1){
            cansaved = false;
            msg = "Isi Pegawai";
            elemtt.focus();
        }
        
        if (cansaved) {
            
            $.post('${baseURL}penjualan/validation', thisform.serialize(),function(data){
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
     beforeShow: function(input, inst)
       {
          inst.dpDiv.css({marginTop: ($( "#tanggal" ).offset().top-190) + 'px', marginLeft: '0px'});
       },       
    dateFormat: 'dd/mm/yy'
    });
    $('#pelanggan')
        .textext({
            plugins : '<c:if test="${empty dataEdit}">prompt </c:if>autocomplete ajax',
            <c:if test="${empty dataEdit}">
            prompt  : 'Pelanggan',
            </c:if>
            ajax : {
                url : '${baseURL}pelanggan.json',
                dataType : 'json'                
            }
        });
    $('#pegawai')
        .textext({
            plugins : '<c:if test="${empty dataEdit}">prompt </c:if>autocomplete ajax',
            <c:if test="${empty dataEdit}">
            prompt  : 'Pegawai',
            </c:if>
            ajax : {
                url : '${baseURL}pegawai.json',
                dataType : 'json'                
            }
        });
   $('#btnTambahBarang').click(function(e) {
   
   elmenttvall = $('#tbodyitem').children().last().children().eq(0).find('input[name="kodebarang"]').val();
   $('#tbodyitem').children().last().children().eq(0).html(elmenttvall+'<input type="hidden" name="kodebarang" value="'+elmenttvall+'">');
   elmenttvall = $('#tbodyitem').children().last().children().eq(1).find('input[name="namabarang"]').val();
   $('#tbodyitem').children().last().children().eq(1).html(elmenttvall+'<input type="hidden" name="namabarang" value="'+elmenttvall+'">');
      
   komponen = '<tr>'
	+'<td><textarea id="kodebarang" name="kodebarang" rows="1"></textarea></td>'
	+'<td><textarea id="namabarang" name="namabarang" rows="1"></textarea></td>'
	+'<td><input type="text" class="form-control numberfilter jumlah" name="jumlah" style="text-align: right;"/></td>'
	+'<td><input type="text" class="form-control numberfilter harga" name="harga" style="text-align: right;"/></td>'
	+'<td><input type="text" class="form-control numberfilter diskon" value="0" name="diskon" style="text-align: right;"/></td>'
	+'<td><input type="text" class="form-control numberfilter total" value="0" name="total" style="text-align: right;"/></td>'
        +'<td><button type="button" class="btn btn-danger btdeleteitem">Delete</button></td>'
	+'</tr>';
    
   $('#tbodyitem').append(komponen);
   refreshautocompletebarang();
   });
   $('body').on('click','.btdeleteitem',function(){
       $(this).parent().parent().remove();
   });
   jQuery('textarea').keydown(function(event) {
        if (event.keyCode == 13) {
            $('.form-horizontal').submit();
         }
    });
    
   refreshautocompletebarang();
   
   $('table').on('keyup','.jumlah',function() {
       countdata();
   });
   
   $('table').on('keyup','.harga',function() {
       countdata();
   });
   
   $('table').on('keyup','.diskon',function() {
       countdata();
   });
   </c:if>
   <c:if test="${!empty dataEdit}">
   $('.numberfilter').each(function() {
       $(this).val(addCommas($(this).val()));
   });
</c:if>
});
</script>