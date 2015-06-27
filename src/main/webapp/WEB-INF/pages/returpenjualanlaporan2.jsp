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
                    <label for="tanggal" class="col-xs-2 control-label">Tanggal</label>
                    <div class="col-xs-10">
                        <input type="text" class="form-control" id="tanggal" name="tanggal" style="width:130px" placeholder="Tanggal"<c:if test="${!empty dataEdit}"> value="${dataEdit.tanggal}"</c:if>>
                    </div>
                </div>
                <div class="form-group">
                    <label for="pelanggan" class="col-xs-2 control-label">Pegawai</label>
                    <div class="col-xs-10">
                        <input type="text" class="form-control" id="pelanggan" name="pelanggan" placeholder="Pegawai"<c:if test="${!empty dataEdit}"> value="${dataEdit.pelanggan}"</c:if>>
                    </div>
                </div>
                <!--            <div class="form-group">
                                <label for="pegawai" class="col-xs-2 control-label">Pegawai</label>
                                <div class="col-xs-10">
                                    <input type="text" class="form-control" id="pegawai" name="pegawai" placeholder="Pegawai"<c:if test="${!empty dataEdit}"> value="${dataEdit.pegawai}"</c:if>>
                                </div>
                            </div>-->
                <br/><br/>

                <div class="row">
                    <div class="col-xs-12">
                        <table class="table table-striped">
                            <colgroup>
                                <col width="120" />
                                <col />
                                <col width="100" />

                            </colgroup>
                            <thead>
                                <tr>
                                    <th>Kode Barang</th>
                                    <th>Nama Barang</th>
                                    <th>Jumlah</th>

                                </tr>
                            </thead>
                            <tbody id="tbodyitem">
                            <c:forEach items="${dataEdit.detailData}" var="data1">
                                <tr>
                                    <td>${data1.kode_barang}</td>
                                    <td>${data1.nama_barang}</td>
                                    <td>${data1.jumlah}</td>
                                </tr>
                            </c:forEach>

                        </tbody>

                    </table>
                </div>
            </div>

            <br/>

        </form>
    </div>
</div>
<script>
    function makereadonly(elementtt) {
        elementtt.attr('readonly', true);
        elementtt.css('background-color', '#fff');
        elementtt.css('border', 'none');
    }
    var cacheBarang = {};
    function refreshautocompletebarang() {
        $('#namabarang').autocomplete({
            minLength: 0,
            source: function (request, response) {
                var term = request.term;
                if (term[0] == "(" && term.indexOf(")", 1) != -1)
                    request.term = term = term.substring(1, term.indexOf(")", 1));
                if (term in cacheBarang) {
                    response(cacheBarang[ term ]);
                    return;
                }

                $.getJSON("${baseURL}barang.json", request, function (data, status, xhr) {
                    cacheBarang[ term ] = data;
                    response(data);
                });
            },
            select: function (event, ui) {
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

    function addCommas(sValue) {
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

    function countdata() {
        $('table .jumlah').each(function () {
            var jumlah = $(this).parent().parent().children().eq(2).children().val().replace(/\./g, '');
            var harga = $(this).parent().parent().children().eq(3).children().val().replace(/\./g, '');
            var diskon = $(this).parent().parent().children().eq(4).children().val().replace(/\./g, '');
            var hargasetelahdiskon = harga - (harga * diskon / 100);
            var total = jumlah * hargasetelahdiskon;
            total = addCommas(total.toString());
            var totalelement = $(this).parent().parent().children().eq(5).children();
            totalelement.val(total);
        });
    }
    $(document).ready(function () {
    <c:if test="${!empty isDetail}">
        makereadonly($('#tanggal'));
        makereadonly($('#pelanggan'));
        makereadonly($('#pegawai'));
        makereadonly($('.numberfilter'));
    </c:if>

        makereadonly($('#no_faktur'));

    <c:if test="${empty isDetail}">
        $('.form-horizontal').submit(function (e) {
            e.preventDefault();
            var thisform = $(this);
            cansaved = true;
            msg = "";
            elemtt = $('#no_faktur');
            if (cansaved && elemtt.val().length < 1) {
                cansaved = false;
                msg = "Isi Nomor Faktur";
                elemtt.focus();
            }
            elemtt = $('#tanggal');
            if (cansaved && elemtt.val().length < 1) {
                cansaved = false;
                msg = "Isi Tanggal";
                elemtt.focus();
            }
            elemtt = $('#pelanggan');
            if (cansaved && elemtt.val().length < 1) {
                cansaved = false;
                msg = "Isi Pelanggan";
                elemtt.focus();
            }
            elemtt = $('#pegawai');
            if (cansaved && elemtt.val().length < 1) {
                cansaved = false;
                msg = "Isi Pegawai";
                elemtt.focus();
            }

            if (cansaved) {
                $.post('${baseURL}penjualan/validation', thisform.serialize(), function (data) {
                    var jobj = data;
                    if (jobj.cansaved == 1) {
                        thisform.unbind();
                        thisform.submit();
                    } else {
                        $('#msg').text(jobj.msg);
                        $('#msg').css('display', 'block');
                    }
                });
            }
            else {
                $('#msg').text(msg);
                $('#msg').css('display', 'block');
            }
        });
        $('body').on('keydown', '.numberfilter', function (e) {
            if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
                    (e.keyCode == 65 && e.ctrlKey === true) ||
                    (e.keyCode >= 35 && e.keyCode <= 39)) {
                return;
            }
            if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                e.preventDefault();
            }
        });

        $('body').on('keyup', '.numberfilter', function (e) {
            $(this).val(addCommas($(this).val()));
        });

        $("#tanggal").datepicker({
            beforeShow: function (input, inst) {
                inst.dpDiv.css({marginTop: ($("#tanggal").offset().top - 190) + 'px', marginLeft: '0px'});
            },
            dateFormat: 'dd/mm/yy'
        });
        var cachePelanggan = {};
        $('#pelanggan').autocomplete({
            minLength: 0,
            source: function (request, response) {
                var term = request.term;
                if (term[0] == "(" && term.indexOf(")", 1) != -1)
                    request.term = term = term.substring(1, term.indexOf(")", 1));
                if (term in cachePelanggan) {
                    response(cachePelanggan[ term ]);
                    return;
                }

                $.getJSON("${baseURL}pelanggan.json", request, function (data, status, xhr) {
                    cachePelanggan[ term ] = data;
                    response(data);
                });
            }
        });
        var cachePegawai = {};
        $('#pegawai').autocomplete({
            minLength: 0,
            source: function (request, response) {
                var term = request.term;
                if (term[0] == "(" && term.indexOf(")", 1) != -1)
                    request.term = term = term.substring(1, term.indexOf(")", 1));
                if (term in cachePegawai) {
                    response(cachePegawai[ term ]);
                    return;
                }

                $.getJSON("${baseURL}pegawai.json", request, function (data, status, xhr) {
                    cachePegawai[ term ] = data;
                    response(data);
                });
            }
        });
        $('#btnTambahBarang').click(function (e) {
            elmenttvall = $('#tbodyitem').children().last().children().eq(0).find('input[name="kodebarang"]').val();
            $('#tbodyitem').children().last().children().eq(0).html(elmenttvall + '<input type="hidden" name="kodebarang" value="' + elmenttvall + '">');
            elmenttvall = $('#tbodyitem').children().last().children().eq(1).find('input[name="namabarang"]').val();
            $('#tbodyitem').children().last().children().eq(1).html(elmenttvall + '<input type="hidden" name="namabarang" value="' + elmenttvall + '">');

            komponen = '<tr>'
                    + '<td><span id="kodebarang_text"></span><input type="hidden" id="kodebarang" name="kodebarang"></td>'
                    + '<td><input type="text" class="form-control" id="namabarang" name="namabarang" placeholder="Barang"></td>'
                    + '<td><input type="text" class="form-control numberfilter jumlah" name="jumlah" style="text-align: right;"/></td>'
                    + '<td><input type="text" class="form-control numberfilter harga" name="harga" style="text-align: right;"/></td>'
                    + '<td><input type="text" class="form-control numberfilter diskon" value="0" name="diskon" style="text-align: right;"/></td>'
                    + '<td><input type="text" class="form-control numberfilter total" value="0" name="total" style="text-align: right;"/></td>'
                    + '<td><button type="button" class="btn btn-danger btdeleteitem">Delete</button></td>'
                    + '</tr>';

            $('#tbodyitem').append(komponen);
            refreshautocompletebarang();
        });
        $('body').on('click', '.btdeleteitem', function () {
            $(this).parent().parent().remove();
        });

        refreshautocompletebarang();

        $('table').on('keyup', '.jumlah', function () {
            countdata();
        });

        $('table').on('keyup', '.harga', function () {
            countdata();
        });

        $('table').on('keyup', '.diskon', function () {
            countdata();
        });
    </c:if>
    <c:if test="${!empty dataEdit}">
        $('.numberfilter').each(function () {
            $(this).val(addCommas($(this).val()));
        });
    </c:if>
    });
</script>
<%@include file="/WEB-INF/layout/footer.jsp" %>
