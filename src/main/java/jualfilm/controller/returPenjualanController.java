/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jualfilm.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import modelDatabase.hibernateUtil;

import javax.servlet.http.HttpServletRequest;
import modelDatabase.Util;

import modelDatabase.barang;
import modelDatabase.detail_penjualan;
import modelDatabase.pegawai;
import modelDatabase.pelanggan;
import modelDatabase.penjualan;
import modelDatabase.purchase_order;
import modelDatabase.retur_pembelian;
import modelDatabase.retur_pembelian_detail;
import modelDatabase.retur_penjualan;
import modelDatabase.retur_penjualan_detail;
import modelDatabase.supplier;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.bind.annotation.ResponseBody;
import org.json.JSONObject;

/**
 *
 * @author ade
 */
@Controller
public class returPenjualanController {

    @RequestMapping(value = "retur-penjualan", method = RequestMethod.GET)
    public String dataList(ModelMap model) {
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_penjualan.class);
        List<retur_penjualan> lreturPenjualan = criteria.list();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        List showData = new ArrayList();
        for (retur_penjualan rpp : lreturPenjualan) {
            Map mapData = new HashMap();
            mapData.put("no_retur_penjualan", rpp.getNo_returpenjualan());
            mapData.put("tanggal", df.format(rpp.getTanggal()));
            
            //mapData.put("no_faktur", rpp.getNo_faktur().getNo_faktur());
            try {
                mapData.put("pegawai", "(" + rpp.getId_pegawai().getId_pegawai() + ") " + rpp.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {

            }
            showData.add(mapData);
        }

        model.addAttribute("dataList", showData);
        session.close();
        return "returPenjualanList";
    }

    @RequestMapping(value = "retur-penjualan/so/{kode}", method = RequestMethod.GET)
    public String displayBySO(ModelMap model, @PathVariable(value = "kode") String kode) {
        penjualanController penjualanController = new penjualanController();
        //model = penjualanController.getDataPenjualan(model, kode);
        model = getDataPenjualanInRetur(model, kode);
        return "returPenjualanAdd2";
    }

    @RequestMapping(value = "retur-penjualan/so/{kode}", method = RequestMethod.POST)
    public String DOdataAdd(ModelMap model, HttpServletRequest request) {
        String no_faktur = request.getParameter("no_faktur");
        String tanggal = request.getParameter("tanggal");
        String pelanggan = request.getParameter("pelanggan");
        String pegawai = request.getParameter("pegawai");
        pegawai = pegawai.substring((pegawai.indexOf("(") + 1), pegawai.indexOf(")"));
        pelanggan = pelanggan.substring((pelanggan.indexOf("(") + 1), pelanggan.indexOf(")"));

        String[] kodeparameter = request.getParameterValues("kodebarang");
        String[] namabarang = request.getParameterValues("namabarang");
        String[] jumlah = request.getParameterValues("jumlah");
        String[] harga = request.getParameterValues("harga");
        String[] diskon = request.getParameterValues("diskon");
        String[] total = request.getParameterValues("total");
        int lengthDAta = kodeparameter.length;

        Session session = hibernateUtil.getSessionFactory().openSession();

        pegawai pegawaiIn = new pegawai();
        Criteria criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.eq("id_pegawai", pegawai));
        if (criteria.uniqueResult() != null) {
            pegawaiIn = (pegawai) criteria.uniqueResult();
        }

//        pelanggan pelangganIn = new pelanggan();
//        criteria = session.createCriteria(pelanggan.class);
//        criteria.add(Restrictions.eq("kode_pelanggan", pelanggan));
//        if (criteria.uniqueResult() != null) {
//            pelangganIn = (pelanggan) criteria.uniqueResult();
//        }

        penjualan penjualan = new penjualan();
        criteria = session.createCriteria(penjualan.class);
        criteria.add(Restrictions.eq("no_faktur", no_faktur));
        if (criteria.uniqueResult() != null) {
            penjualan = (penjualan) criteria.uniqueResult();
        }

        Transaction trx = session.beginTransaction();

        retur_penjualan pj = new retur_penjualan();
        //pj.setNo_returpenjualan("RJAL-0011");
        pj.setNo_faktur(penjualan.getNo_faktur());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            pj.setTanggal(new Timestamp(df.parse(tanggal).getTime()));
        } catch (ParseException ex) {
            Logger.getLogger(purchaseOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }

        pj.setId_pegawai(pegawaiIn);
        //session.saveOrUpdate(pj);

        int i = 0;
        for (i = 0; i < lengthDAta; i++) {
            if (kodeparameter[i].length() > 0
                    && namabarang[i].length() > 0
                    && jumlah[i].length() > 0
                    && harga[i].length() > 0
                    && total[i].length() > 0) {
                String jmlBarang = jumlah[i].replace(".", "");
                try {
                    int jmlBarangl = Integer.valueOf(jmlBarang);
                    if (jmlBarangl > 0) {
                        barang barangIn = new barang();
                        criteria = session.createCriteria(barang.class);
                        criteria.add(Restrictions.eq("kode_barang", kodeparameter[i]));
                        if (criteria.uniqueResult() != null) {
                            barangIn = (barang) criteria.uniqueResult();
                        }

                        Criteria returPenjualanCriteria = session.createCriteria(retur_penjualan.class);
                        returPenjualanCriteria.add(Restrictions.eq("no_faktur", no_faktur));
                        if (returPenjualanCriteria.uniqueResult() == null) // tidak ada di database
                        {
                            Criteria tem = session.createCriteria(retur_penjualan.class);
                            criteria.addOrder(Order.desc("id"));
                            criteria.setMaxResults(1);

                            final String monthYear = Util.convertTime(System.currentTimeMillis());
                            final String prefix = "RJAL" + monthYear + "-";
                            String kodedata = prefix + "0001";

                            if (tem.uniqueResult() != null) {
                                kodedata = String.valueOf(Integer.valueOf(tem.uniqueResult().toString()) + 1);
                                while (kodedata.length() < 4) {
                                    kodedata = "0" + kodedata;
                                }
                            }
                            //kodedata = prefix + kodedata;
                            pj.setNo_returpenjualan(kodedata);
                            pj.setTanggal(new java.sql.Timestamp(new Date().getTime()));

                            session.saveOrUpdate(pj);
                            retur_penjualan_detail returPenjualanDetail = new retur_penjualan_detail();
                            returPenjualanDetail.setJumlah(jmlBarangl);
                            returPenjualanDetail.setKode_barang(barangIn);
                            returPenjualanDetail.setReturPenjualan(pj);
                            session.save(returPenjualanDetail);

                        } else //ada di database
                        {
                            retur_penjualan rPenjualan = (retur_penjualan) returPenjualanCriteria.uniqueResult();
                            final String no_retur = rPenjualan.getNo_returpenjualan();
                            Criteria returPenjualanDetailCriteria = session.createCriteria(retur_penjualan_detail.class);
                            returPenjualanDetailCriteria.
                                    add(Restrictions.eq("no_returpenjualan.no_returpenjualan", no_retur)).
                                    add(Restrictions.eq("kode_barang.id", barangIn.getId()));
                            System.out.println("bbbbbbbbb " + no_retur + "");

                            if (returPenjualanDetailCriteria.uniqueResult() != null) {
                                retur_penjualan_detail returPenjualanDetail = (retur_penjualan_detail) returPenjualanDetailCriteria.uniqueResult();

                                returPenjualanDetail.setJumlah(jmlBarangl);
                                returPenjualanDetail.setKode_barang(barangIn);
                                returPenjualanDetail.setReturPenjualan(rPenjualan);
                                session.saveOrUpdate(returPenjualanDetail);
                            } else {
                                retur_penjualan_detail returPenjualanDetail = new retur_penjualan_detail();
                                returPenjualanDetail.setJumlah(jmlBarangl);
                                returPenjualanDetail.setKode_barang(barangIn);
                                returPenjualanDetail.setReturPenjualan(rPenjualan);
                                session.save(returPenjualanDetail);
                            }

                        }
                    }

                } catch (Exception ex) {
                    System.out.println(" error add po " + ex.getMessage());
                }
            }
        }

        trx.commit();

        session.close();

        return "redirect:/penjualan";
    }

    public ModelMap getDataPenjualanInRetur(ModelMap model, String kode) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(penjualan.class
        );
        criteria.add(Restrictions.eq("no_faktur", kode));
        if (criteria.uniqueResult()
                != null) {
            penjualan pj = (penjualan) criteria.uniqueResult();
            Map modelHere = new HashMap();
            modelHere.put("no_faktur", pj.getNo_faktur());
            modelHere.put("tanggal", df.format(pj.getTanggal()));
            try {
                modelHere.put("pelanggan", "(" + pj.getKode_pelanggan().getKode_pelanggan() + ") " + pj.getKode_pelanggan().getNama_pelanggan());
            } catch (Exception ex) {

            }
            try {
                modelHere.put("pegawai", "(" + pj.getId_pegawai().getId_pegawai() + ") " + pj.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {

            }

            List<detail_penjualan> ldpo = pj.getPenjualan_detail();
            List detailData = new ArrayList();
            for (detail_penjualan dpe : ldpo) {
                Map modelHere1 = new HashMap();
                modelHere1.put("kode_barang", dpe.getKode_barang().getKode_barang());
                modelHere1.put("nama_barang", dpe.getNama_barang());

                modelHere1.put("harga", dpe.getHarga());
                modelHere1.put("diskon", dpe.getDiskon());
                modelHere1.put("total", dpe.getTotal());
                Criteria returCriteria = session.createCriteria(retur_penjualan.class);
                returCriteria.add(Restrictions.eq("no_faktur", pj.getNo_faktur()));

//                retur_penjualan rj = (retur_penjualan) returCriteria.uniqueResult();
//                if(rj == null)
//                {
//                    //retur_penjualan_detail rpd = rj.getReturPenjualanDetail().get(0);
//                    modelHere1.put("jumlah", 0);
//                }
//                else
//                {
//                    retur_penjualan_detail rpd = rj.getReturPenjualanDetail().get(0);
//                    modelHere1.put("jumlah", rpd.getJumlah());
//                }
                Query query = session.createSQLQuery(
                        "SELECT rpd.jumlah FROM retur_penjualan_detail rpd, retur_penjualan rp "
                        + " WHERE rp.no_faktur = '" + pj.getNo_faktur() + "'"
                        + " and rp.no_returpenjualan = rpd.no_returpenjualan and "
                        + " rpd.id_barang = '" + dpe.getKode_barang().getId() + "'");

                List result = query.list();
                if (result == null || result.size() == 0) {
                    modelHere1.put("jumlah", "0");
                } else {
                    modelHere1.put("jumlah", result.get(0));
                }

                detailData.add(modelHere1);
            }
            modelHere.put("detailData", detailData);
            model.addAttribute("dataEdit", modelHere);
            model.addAttribute("isDetail", true);

        }

        session.close();

        model.addAttribute(
                "headerapps", "Detail Nota Penjualan");
        return model;
    }

    @RequestMapping(value = "retur-penjualan/laporan", method = RequestMethod.GET)
    public String dataList2(ModelMap model, HttpServletRequest req) {
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_penjualan.class
        );
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            if (req.getParameter("from") != null && req.getParameter("to") != null) {
                Timestamp starDate = new Timestamp(df.parse(req.getParameter("from") + " 00:00:00").getTime());
                Timestamp toDate = new Timestamp(df.parse(req.getParameter("to") + " 59:59:59").getTime());
                criteria.add(Restrictions.between("tanggal", starDate, toDate));
                model.addAttribute("startDate", req.getParameter("from"));
                model.addAttribute("endDate", req.getParameter("to"));
            }

        } catch (Exception ex) {
            System.out.println(" err dataLaporanList " + ex.getMessage());
        }

        List<retur_penjualan> lreturPenjualan = criteria.list();
        List showData = new ArrayList();
        df = new SimpleDateFormat("dd/MM/yyyy");
        for (retur_penjualan rpp : lreturPenjualan) {
            Map mapData = new HashMap();
            mapData.put("no_retur_penjualan", rpp.getNo_returpenjualan());
            mapData.put("tanggal", df.format(rpp.getTanggal()));
            //mapData.put("no_faktur", rpp.getNo_faktur().getNo_faktur());
            try {
                mapData.put("pegawai", "(" + rpp.getId_pegawai().getId_pegawai() + ") " + rpp.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {

            }
            showData.add(mapData);
        }

        model.addAttribute(
                "dataList", showData);
        session.close();

        return "returPenjualanLaporan";
    }

    @RequestMapping(value = "retur-penjualan/delete", method = RequestMethod.GET)
    public String dataAdd(ModelMap model, HttpServletRequest request) {
        String kodesupplier = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_penjualan.class
        );
        criteria.add(Restrictions.eq("no_returpenjualan", kodesupplier));
        retur_penjualan rpp = (retur_penjualan) criteria.uniqueResult();
        Transaction trx = session.beginTransaction();

        session.delete(rpp);

        trx.commit();

        session.close();

        return "redirect:/retur-penjualan";
    }

    @RequestMapping(value = "retur-penjualan/add", method = RequestMethod.GET)
    public String dataAdd(ModelMap model) {
        model.addAttribute("headerapps", "Retur Penjualan Baru");

        Map mapDAta = new HashMap();
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_penjualan.class
        ).setProjection(Projections.property("id"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(
                1);
        String kodedata = "RJAL-0001";

        if (criteria.uniqueResult()
                != null) {
            kodedata = String.valueOf(Integer.valueOf(criteria.uniqueResult().toString()) + 1);
            while (kodedata.length() < 4) {
                kodedata = "0" + kodedata;
            }
            kodedata = "RJAL-" + kodedata;
        }

        mapDAta.put(
                "no_returpenjualan", kodedata);
        model.addAttribute(
                "dataEdit", mapDAta);
        session.close();

        return "returPenjualanAdd";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "retur-penjualan/add", method = RequestMethod.POST)
    public String DOdataAdd2(ModelMap model, HttpServletRequest request) {
        String no_returpenjualan = request.getParameter("no_returpenjualan");
        String tanggal = request.getParameter("tanggal");
        String no_faktur = request.getParameter("no_faktur");
        String jumlah = request.getParameter("jumlah");
        if (jumlah.length() > 0) {
            jumlah = jumlah.replace(".", "");
        }

        if (no_faktur != null) {
            if (no_faktur.length() > 0) {
                char ch = (char) 34;
                String komponen = String.valueOf(ch);

                if (no_faktur.substring(0, 1).equals("\"")) {
                    no_faktur = no_faktur.substring(1);
                }

                if (no_faktur.substring(no_faktur.length() - 1).equals("\"")) {
                    no_faktur = no_faktur.substring(0, no_faktur.length() - 1);
                }

            }
        }

        String pegawai = request.getParameter("pegawai");
        if (pegawai != null) {
            if (pegawai.length() > 0) {
                pegawai = pegawai.substring((pegawai.indexOf("(") + 1), pegawai.indexOf(")"));
            }
        }

        String pelanggan = request.getParameter("pelanggan");
        if (pelanggan != null) {
            if (pelanggan.length() > 0) {
                pelanggan = pelanggan.substring((pelanggan.indexOf("(") + 1), pelanggan.indexOf(")"));
            }
        }
        String kode_barang = request.getParameter("kode_barang");
        String nama_barang = "";
        if (kode_barang != null) {
            if (kode_barang.length() > 0) {
                kode_barang = kode_barang.substring((kode_barang.indexOf("(") + 1), kode_barang.indexOf(")"));
                nama_barang = kode_barang.substring((kode_barang.indexOf(")") + 2));
            }
        }

        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction trx = session.beginTransaction();

        retur_penjualan rpp = new retur_penjualan();

        pegawai pegawaiIn = new pegawai();
        Criteria criteria = session.createCriteria(pegawai.class
        );
        criteria.add(Restrictions.eq("id_pegawai", pegawai));
        if (criteria.uniqueResult()
                != null) {
            pegawaiIn = (pegawai) criteria.uniqueResult();
        }

        pelanggan pelangganIn = new pelanggan();
        criteria = session.createCriteria(pelanggan.class);

        criteria.add(Restrictions.eq("kode_pelanggan", pelanggan));
        if (criteria.uniqueResult()
                != null) {
            pelangganIn = (pelanggan) criteria.uniqueResult();
        }

        barang barangIn = new barang();
        criteria = session.createCriteria(barang.class);

        criteria.add(Restrictions.eq("kode_barang", kode_barang));
        if (criteria.uniqueResult()
                != null) {
            barangIn = (barang) criteria.uniqueResult();
        }

        penjualan penjualanIn = new penjualan();
        criteria = session.createCriteria(penjualan.class);

        criteria.add(Restrictions.eq("no_faktur", no_faktur));
        if (criteria.uniqueResult()
                != null) {
            penjualanIn = (penjualan) criteria.uniqueResult();
        }

        rpp.setId_pegawai(pegawaiIn);

        //rpp.setNo_faktur(penjualanIn);
        rpp.setNo_returpenjualan(no_returpenjualan);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        try {
            rpp.setTanggal(new Timestamp(df.parse(tanggal).getTime()));
        } catch (Exception ex) {

        }

        session.save(rpp);

        trx.commit();

        session.close();

        return "redirect:/retur-penjualan";
    }

    @RequestMapping(value = "retur-penjualan/validation", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String ValidationdataAdd(ModelMap model, HttpServletRequest request) {
        String msg = "";
        int cansaved = 1;
        JSONObject jobj = new JSONObject();
        String kodesupplier = request.getParameter("no_returpenjualan");

        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_penjualan.class
        ).setProjection(Projections.rowCount());
        if (request.getParameter(
                "no_returpenjualan1") != null) {
            criteria.add(Restrictions.ne("no_returpenjualan", request.getParameter("no_returpenjualan1").toString()));
        }

        criteria.add(Restrictions.eq("no_returpenjualan", kodesupplier));

        if (Integer.valueOf(criteria.uniqueResult().toString()) > 0) {
            cansaved = 0;
            msg = "Kode " + kodesupplier + " telah digunakan oleh Retur Penjualan lain";
        }

        try {
            jobj.put("msg", msg);
            jobj.put("cansaved", cansaved);
        } catch (Exception ex) {

        }
        session.close();

        return jobj.toString();
    }

    @RequestMapping(value = "retur-penjualan/edit", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, HttpServletRequest request) {
        String returndata = "redirect:/retur-penjualan";
        String kodesupplier = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_penjualan.class
        );
        criteria.add(Restrictions.eq("no_returpenjualan", kodesupplier));
        if (criteria.uniqueResult()
                != null) {
            retur_penjualan rpe = (retur_penjualan) criteria.uniqueResult();
            Map modelData = new HashMap();
            modelData.put("no_returpenjualan", rpe.getNo_returpenjualan());
            modelData.put("no_returpenjualan1", rpe.getNo_returpenjualan());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            modelData.put("tanggal", df.format(rpe.getTanggal()));
            //modelData.put("no_faktur", rpe.getNo_faktur().getNo_faktur());
            try {
                modelData.put("pegawai", "(" + rpe.getId_pegawai().getId_pegawai() + ") " + rpe.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {

            }
//            try {
//                modelData.put("kode_barang", "("+ rpe.getKode_barang().getKode_barang()+") "+ rpe.getKode_barang().getNama_barang());
//            } catch (Exception ex) {
//                System.out.println(" error "+ex.getMessage());
//            }
            model.addAttribute("dataEdit", modelData);
            returndata = "returPenjualanAdd";
        }

        session.close();

        model.addAttribute(
                "headerapps", "Edit Retur Penjualan");
        return returndata;
    }

    @RequestMapping(value = "retur-penjualan/edit", method = RequestMethod.POST)
    public String DOdataEdit(ModelMap model, HttpServletRequest request) {
        String returndata = "redirect:/retur-penjualan";
        String no_returpenjualan = request.getParameter("no_returpenjualan");
        String no_returpenjualan1 = request.getParameter("no_returpenjualan1");
        String tanggal = request.getParameter("tanggal");
        String no_faktur = request.getParameter("no_faktur");
        System.out.println(" no_faktur = " + no_faktur);
        String jumlah = request.getParameter("jumlah");
        if (jumlah.length() > 0) {
            jumlah = jumlah.replace(".", "");
        }
        String nama_barang = request.getParameter("nama_barang");

        if (no_faktur != null) {
            if (no_faktur.length() > 0) {
                char ch = (char) 34;
                String komponen = String.valueOf(ch);

                if (no_faktur.substring(0, 1).equals("\"")) {
                    no_faktur = no_faktur.substring(1);
                }

                if (no_faktur.substring(no_faktur.length() - 1).equals("\"")) {
                    no_faktur = no_faktur.substring(0, no_faktur.length() - 1);
                }

            }
        }

        String pegawai = request.getParameter("pegawai");
        if (pegawai != null) {
            if (pegawai.length() > 0) {
                pegawai = pegawai.substring((pegawai.indexOf("(") + 1), pegawai.indexOf(")"));
            }
        }

        String pelanggan = request.getParameter("pelanggan");
        if (pelanggan != null) {
            if (pelanggan.length() > 0) {
                pelanggan = pelanggan.substring((pelanggan.indexOf("(") + 1), pelanggan.indexOf(")"));
            }
        }
        String kode_barang = request.getParameter("kode_barang");
        if (kode_barang != null) {
            if (kode_barang.length() > 0) {
                kode_barang = kode_barang.substring((kode_barang.indexOf("(") + 1), kode_barang.indexOf(")"));
            }
        }

        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_penjualan.class
        );
        criteria.add(Restrictions.eq("no_returpenjualan", no_returpenjualan1));
        if (criteria.uniqueResult()
                != null) {
            Transaction trx = session.beginTransaction();
            retur_penjualan rpp = (retur_penjualan) criteria.uniqueResult();

            pegawai pegawaiIn = new pegawai();
            criteria = session.createCriteria(pegawai.class);
            criteria.add(Restrictions.eq("id_pegawai", pegawai));
            if (criteria.uniqueResult() != null) {
                pegawaiIn = (pegawai) criteria.uniqueResult();
            }

            pelanggan pelangganIn = new pelanggan();
            criteria = session.createCriteria(pelanggan.class);
            criteria.add(Restrictions.eq("kode_pelanggan", pelanggan));
            if (criteria.uniqueResult() != null) {
                pelangganIn = (pelanggan) criteria.uniqueResult();
            }

            barang barangIn = new barang();
            criteria = session.createCriteria(barang.class);
            criteria.add(Restrictions.eq("kode_barang", kode_barang));
            if (criteria.uniqueResult() != null) {
                barangIn = (barang) criteria.uniqueResult();
            }

            penjualan penjualanIn = new penjualan();
            criteria = session.createCriteria(penjualan.class);
            criteria.add(Restrictions.eq("no_faktur", no_faktur));
            if (criteria.uniqueResult() != null) {
                penjualanIn = (penjualan) criteria.uniqueResult();
            }

            rpp.setId_pegawai(pegawaiIn);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                rpp.setTanggal(new Timestamp(df.parse(tanggal).getTime()));
            } catch (Exception ex) {

            }

            session.update(rpp);

            if (!no_returpenjualan1.equalsIgnoreCase(no_returpenjualan)) {
                String sql = "update retur_penjualan set no_returpenjualan=:kode where no_returpenjualan=:kode1";
                session.createQuery(sql).setParameter("kode", no_returpenjualan)
                        .setParameter("kode1", no_returpenjualan1).executeUpdate();
            }
            trx.commit();
        }

        session.close();
        return returndata;
    }

    @RequestMapping(value = "retur-penjualan/one-detail/{kode}", method = RequestMethod.GET)
    public String dataEditREturPenjualan(ModelMap model, HttpServletRequest request,
            @PathVariable(value = "kode") String kode) {
        String returndata = "redirect:/retur-penjualan";
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_penjualan.class
        );
        criteria.add(Restrictions.eq("no_returpenjualan", kode));
        if (criteria.uniqueResult()
                != null) {
            retur_penjualan rpe = (retur_penjualan) criteria.uniqueResult();
            Map modelData = new HashMap();
            modelData.put("no_returpenjualan", rpe.getNo_returpenjualan());
            modelData.put("no_returpenjualan1", rpe.getNo_returpenjualan());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            modelData.put("tanggal", df.format(rpe.getTanggal()));
            //modelData.put("no_faktur", rpe.getNo_faktur().getNo_faktur());
            try {
                modelData.put("pegawai", "(" + rpe.getId_pegawai().getId_pegawai() + ") " + rpe.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {

            }
//            try {
//                modelData.put("kode_barang", "("+ rpe.getKode_barang().getKode_barang()+") "+ rpe.getKode_barang().getNama_barang());
//            } catch (Exception ex) {
//                System.out.println(" error "+ex.getMessage());
//            }
            model.addAttribute("isDetail", true);
            model.addAttribute("dataEdit", modelData);
            returndata = "returPenjualanDetailLaporan";
        }

        session.close();

        model.addAttribute(
                "headerapps", "Detail Retur Penjualan");
        return returndata;
    }
    
    @RequestMapping(value = "retur-penjualan/laporan/{kode}", method = RequestMethod.GET)
    public String displayReport(ModelMap model, @PathVariable(value = "kode") String kode) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria returPembelianCriteria = session.createCriteria(retur_penjualan.class);
        returPembelianCriteria.add(Restrictions.eq("no_returpenjualan", kode));
        if(returPembelianCriteria.uniqueResult() != null)
        {
            retur_penjualan returPembelian = (retur_penjualan) returPembelianCriteria.uniqueResult();
            Map modelHere = new HashMap();
            modelHere.put("no_faktur", returPembelian.getNo_returpenjualan());
            modelHere.put("tanggal", returPembelian.getTanggal());
            modelHere.put("pelanggan", returPembelian.getId_pegawai().getNama_pegawai());
            List detailData = new ArrayList();
            for(retur_penjualan_detail rpd : returPembelian.getReturPenjualanDetail())
            {
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put("kode_barang", rpd.getKode_barang().getKode_barang());
                temp.put("nama_barang", rpd.getKode_barang().getNama_barang());
                temp.put("jumlah", rpd.getJumlah());
                detailData.add(temp);
            }
            modelHere.put("detailData", detailData);
            
            model.addAttribute("dataEdit", modelHere);
            model.addAttribute("isDetail", true);
            model.addAttribute(
                "headerapps", "Detail Retur Penjualan");
        }
        session.close();
        return "returpenjualanlaporan2";
    }
}
