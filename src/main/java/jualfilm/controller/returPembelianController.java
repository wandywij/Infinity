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

import modelDatabase.hibernateUtil;

import javax.servlet.http.HttpServletRequest;

import modelDatabase.barang;
import modelDatabase.pegawai;
import modelDatabase.purchase_order;
import modelDatabase.retur_pembelian;
import modelDatabase.supplier;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.bind.annotation.ResponseBody;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelDatabase.Util;
import modelDatabase.detail_penjualan;
import modelDatabase.detail_purchase_order;
import modelDatabase.pelanggan;
import modelDatabase.penjualan;
import modelDatabase.retur_pembelian_detail;
import modelDatabase.retur_penjualan;
import modelDatabase.retur_penjualan_detail;
import org.hibernate.Query;

import org.hibernate.criterion.Order;

/**
 *
 * @author ade
 */
@Controller
public class returPembelianController {

    @RequestMapping(value = "retur-pembelian", method = RequestMethod.GET)
    public String dataList(ModelMap model) {
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class);
        List<retur_pembelian> lreturPembelian = criteria.list();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        List showData = new ArrayList();
        for (retur_pembelian rpp : lreturPembelian) {
            Map mapData = new HashMap();
            mapData.put("no_retur_pembelian", rpp.getNo_retur_pembelian());
            mapData.put("tanggal", df.format(rpp.getTanggal()));
            mapData.put("no_po", rpp.getNo_po());
            try {
                mapData.put("pegawai", "(" + rpp.getId_pegawai().getId_pegawai() + ") " + rpp.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {

            }
//            try {
//                mapData.put("supplier", "("+ rpp.get().getKode_supplier()+") "+ rpp.getKode_supplier().getNama_supplier());
//            } catch (Exception ex) {
//                
//            }
//            try {
//                mapData.put("barang", "("+ rpp.getKode_barang().getKode_barang()+") "+ rpp.getKode_barang().getNama_barang());
//            } catch (Exception ex) {
//                
//            }
            showData.add(mapData);
        }

        model.addAttribute("dataList", showData);
        session.close();
        return "returPembelianList";
    }

    @RequestMapping(value = "retur-pembelian/delete", method = RequestMethod.GET)
    public String dataAdd(ModelMap model, HttpServletRequest request) {
        String kodesupplier = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class);
        criteria.add(Restrictions.eq("no_retur_pembelian", kodesupplier));
        retur_pembelian rpp = (retur_pembelian) criteria.uniqueResult();
        Transaction trx = session.beginTransaction();
        session.delete(rpp);
        trx.commit();
        session.close();
        return "redirect:/retur-pembelian";
    }

    @RequestMapping(value = "retur-pembelian/add", method = RequestMethod.GET)
    public String dataAdd(ModelMap model) {
        model.addAttribute("headerapps", "Retur Pembelian Baru");

        Map mapDAta = new HashMap();
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class).setProjection(Projections.property("id"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        String kodedata = "RPO-0001";
        if (criteria.uniqueResult() != null) {
            kodedata = String.valueOf(Integer.valueOf(criteria.uniqueResult().toString()) + 1);
            while (kodedata.length() < 4) {
                kodedata = "0" + kodedata;
            }
            kodedata = "RPO-" + kodedata;
        }
        mapDAta.put("no_retur_pembelian", kodedata);
        model.addAttribute("dataEdit", mapDAta);
        session.close();

        return "returPembelianAdd";
    }

    @RequestMapping(value = "retur-pembelian/add", method = RequestMethod.POST)
    public String DOdataAdd(ModelMap model, HttpServletRequest request) {
        String no_retur_pembelian = request.getParameter("no_retur_pembelian");
        String tanggal = request.getParameter("tanggal");
        String no_po = request.getParameter("no_po");

        if (no_po != null) {
            if (no_po.length() > 0) {
                char ch = (char) 34;
                String komponen = String.valueOf(ch);

                if (no_po.substring(0, 1).equals("\"")) {
                    no_po = no_po.substring(1);
                }

                if (no_po.substring(no_po.length() - 1).equals("\"")) {
                    no_po = no_po.substring(0, no_po.length() - 1);
                }

            }
        }

        String pegawai = request.getParameter("pegawai");
        if (pegawai != null) {
            if (pegawai.length() > 0) {
                pegawai = pegawai.substring((pegawai.indexOf("(") + 1), pegawai.indexOf(")"));
            }
        }

        String supplier = request.getParameter("supplier");
        if (supplier != null) {
            if (supplier.length() > 0) {
                supplier = supplier.substring((supplier.indexOf("(") + 1), supplier.indexOf(")"));
            }
        }
        String kode_barang = request.getParameter("kode_barang");
        if (kode_barang != null) {
            if (kode_barang.length() > 0) {
                kode_barang = kode_barang.substring((kode_barang.indexOf("(") + 1), kode_barang.indexOf(")"));
            }
        }

        Session session = hibernateUtil.getSessionFactory().openSession();

        purchase_order purchase_orderIn = new purchase_order();
        Criteria criteria = session.createCriteria(purchase_order.class);
        criteria.add(Restrictions.eq("no_po", no_po));
        if (criteria.uniqueResult() != null) {
            purchase_orderIn = (purchase_order) criteria.uniqueResult();
        }

        supplier supplierIn = new supplier();
        criteria = session.createCriteria(supplier.class);
        criteria.add(Restrictions.eq("kode_supplier", supplier));
        if (criteria.uniqueResult() != null) {
            supplierIn = (supplier) criteria.uniqueResult();
        }

        pegawai pegawaiIn = new pegawai();
        criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.eq("id_pegawai", pegawai));
        if (criteria.uniqueResult() != null) {
            pegawaiIn = (pegawai) criteria.uniqueResult();
        }

        barang barangIn = new barang();
        criteria = session.createCriteria(barang.class);
        criteria.add(Restrictions.eq("kode_barang", kode_barang));
        if (criteria.uniqueResult() != null) {
            barangIn = (barang) criteria.uniqueResult();
        }

        Transaction trx = session.beginTransaction();

        retur_pembelian rpp = new retur_pembelian();
        rpp.setId_pegawai(pegawaiIn);
        //rpp.setKode_barang(barangIn);
        //rpp.setKode_supplier(supplierIn);
        //rpp.setNo_po(purchase_orderIn);
        rpp.setNo_retur_pembelian(no_retur_pembelian);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            rpp.setTanggal(new Timestamp(df.parse(tanggal).getTime()));
        } catch (Exception ex) {

        }

        session.save(rpp);
        trx.commit();
        session.close();

        return "redirect:/retur-pembelian";
    }

    @RequestMapping(value = "retur-pembelian/validation", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String ValidationdataAdd(ModelMap model, HttpServletRequest request) {
        String msg = "";
        int cansaved = 1;
        JSONObject jobj = new JSONObject();
        String kodesupplier = request.getParameter("no_retur_pembelian");

        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class).setProjection(Projections.rowCount());
        if (request.getParameter("no_retur_pembelian1") != null) {
            criteria.add(Restrictions.ne("no_retur_pembelian", request.getParameter("no_retur_pembelian1").toString()));
        }
        criteria.add(Restrictions.eq("no_retur_pembelian", kodesupplier));

        if (Integer.valueOf(criteria.uniqueResult().toString()) > 0) {
            cansaved = 0;
            msg = "Kode " + kodesupplier + " telah digunakan oleh Retur Pembelian lain";
        }
        try {
            jobj.put("msg", msg);
            jobj.put("cansaved", cansaved);
        } catch (Exception ex) {

        }
        session.close();
        return jobj.toString();
    }

    @RequestMapping(value = "retur-pembelian/edit", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, HttpServletRequest request) {
        String returndata = "redirect:/retur-pembelian";
        String kodesupplier = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class);
        criteria.add(Restrictions.eq("no_retur_pembelian", kodesupplier));
        if (criteria.uniqueResult() != null) {
            retur_pembelian rpp = (retur_pembelian) criteria.uniqueResult();
            Map modelData = new HashMap();
            modelData.put("no_retur_pembelian", rpp.getNo_retur_pembelian());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            modelData.put("tanggal", df.format(rpp.getTanggal()));
            //modelData.put("no_po", rpp.getNo_po().);
            try {
                modelData.put("pegawai", "(" + rpp.getId_pegawai().getId_pegawai() + ") " + rpp.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {

            }
//            try {
//                modelData.put("supplier", "("+ rpp.getKode_supplier().getKode_supplier()+") "+ rpp.getKode_supplier().getNama_supplier());
//            } catch (Exception ex) {
//                
//            }
//            try {
//                modelData.put("kode_barang", "("+ rpp.getKode_barang().getKode_barang()+") "+ rpp.getKode_barang().getNama_barang());
//            } catch (Exception ex) {
//                System.out.println(" error "+ex.getMessage());
//            }
            model.addAttribute("dataEdit", modelData);
            returndata = "returPembelianAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Edit Retur Pembelian");
        return returndata;
    }

    @RequestMapping(value = "retur-pembelian/edit", method = RequestMethod.POST)
    public String DOdataEdit(ModelMap model, HttpServletRequest request) {
        String returndata = "redirect:/retur-pembelian";
        String no_retur_pembelian = request.getParameter("no_retur_pembelian");
        String no_retur_pembelian1 = request.getParameter("no_retur_pembelian1");
        String tanggal = request.getParameter("tanggal");
        String no_po = request.getParameter("no_po");

        if (no_po != null) {
            if (no_po.length() > 0) {
                char ch = (char) 34;
                String komponen = String.valueOf(ch);

                if (no_po.substring(0, 1).equals("\"")) {
                    no_po = no_po.substring(1);
                }

                if (no_po.substring(no_po.length() - 1).equals("\"")) {
                    no_po = no_po.substring(0, no_po.length() - 1);
                }

            }
        }
        String pegawai = request.getParameter("pegawai");
        if (pegawai != null) {
            if (pegawai.length() > 0) {
                pegawai = pegawai.substring((pegawai.indexOf("(") + 1), pegawai.indexOf(")"));
            }
        }
        String supplier = request.getParameter("supplier");
        if (supplier != null) {
            if (supplier.length() > 0) {
                supplier = supplier.substring((supplier.indexOf("(") + 1), supplier.indexOf(")"));
            }
        }

        String kode_barang = request.getParameter("kode_barang");
        if (kode_barang != null) {
            if (kode_barang.length() > 0) {
                kode_barang = kode_barang.substring((kode_barang.indexOf("(") + 1), kode_barang.indexOf(")"));
            }
        }

        Session session = hibernateUtil.getSessionFactory().openSession();

        purchase_order purchase_orderIn = new purchase_order();
        Criteria criteria = session.createCriteria(purchase_order.class);
        criteria.add(Restrictions.eq("no_po", no_po));
        if (criteria.uniqueResult() != null) {
            purchase_orderIn = (purchase_order) criteria.uniqueResult();
        }

        supplier supplierIn = new supplier();
        criteria = session.createCriteria(supplier.class);
        criteria.add(Restrictions.eq("kode_supplier", supplier));
        if (criteria.uniqueResult() != null) {
            supplierIn = (supplier) criteria.uniqueResult();
        }

        pegawai pegawaiIn = new pegawai();
        criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.eq("id_pegawai", pegawai));
        if (criteria.uniqueResult() != null) {
            pegawaiIn = (pegawai) criteria.uniqueResult();
        }

        barang barangIn = new barang();
        criteria = session.createCriteria(barang.class);
        criteria.add(Restrictions.eq("kode_barang", kode_barang));
        if (criteria.uniqueResult() != null) {
            barangIn = (barang) criteria.uniqueResult();
        }

        criteria = session.createCriteria(retur_pembelian.class);
        criteria.add(Restrictions.eq("no_retur_pembelian", no_retur_pembelian1));
        if (criteria.uniqueResult() != null) {
            Transaction trx = session.beginTransaction();
            retur_pembelian rpp = (retur_pembelian) criteria.uniqueResult();

            rpp.setId_pegawai(pegawaiIn);
            //rpp.setKode_barang(barangIn);
            //rpp.setKode_supplier(supplierIn);
            // rpp.setNo_po(purchase_orderIn);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                rpp.setTanggal(new Timestamp(df.parse(tanggal).getTime()));
            } catch (Exception ex) {

            }

            session.update(rpp);

            if (!no_retur_pembelian1.equalsIgnoreCase(no_retur_pembelian)) {
                String sql = "update retur_pembelian set no_retur_pembelian=:kode where no_retur_pembelian=:kode1";
                session.createQuery(sql).setParameter("kode", no_retur_pembelian)
                        .setParameter("kode1", no_retur_pembelian1).executeUpdate();
            }
            trx.commit();
        }

        session.close();
        return returndata;
    }

    @RequestMapping(value = "retur-pembelian/one-detail/{kode}", method = RequestMethod.GET)
    public String dataEditDetail(ModelMap model, HttpServletRequest request,
            @PathVariable("kode") String kode) {
        String returndata = "redirect:/retur-pembelian";
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class);
        criteria.add(Restrictions.eq("no_retur_pembelian", kode));
        if (criteria.uniqueResult() != null) {
            retur_pembelian rpp = (retur_pembelian) criteria.uniqueResult();
            Map modelData = new HashMap();
            modelData.put("no_retur_pembelian", rpp.getNo_retur_pembelian());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            modelData.put("tanggal", df.format(rpp.getTanggal()));
            //modelData.put("no_po", rpp.getNo_po().getNo_po());
            try {
                modelData.put("pegawai", "(" + rpp.getId_pegawai().getId_pegawai() + ") " + rpp.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {

            }
//            try {
//                modelData.put("supplier", "("+ rpp.getKode_supplier().getKode_supplier()+") "+ rpp.getKode_supplier().getNama_supplier());
//            } catch (Exception ex) {
//                
//            }
//            try {
//                modelData.put("kode_barang", "("+ rpp.getKode_barang().getKode_barang()+") "+ rpp.getKode_barang().getNama_barang());
//            } catch (Exception ex) {
//                System.out.println(" error "+ex.getMessage());
//            }
            model.addAttribute("dataEdit", modelData);
            model.addAttribute("isDetail", true);
            returndata = "returPembelianAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Detail Retur Pembelian");

        return returndata;
    }

    @RequestMapping(value = "retur-pembelian/laporan", method = RequestMethod.GET)
    public String dataList2(ModelMap model, HttpServletRequest req) {
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class);

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
        List<retur_pembelian> lreturPembelian = criteria.list();
        List showData = new ArrayList();
        df = new SimpleDateFormat("dd/MM/yyyy");
        for (retur_pembelian rpp : lreturPembelian) {
            Map mapData = new HashMap();
            mapData.put("no_retur_pembelian", rpp.getNo_retur_pembelian());
            mapData.put("tanggal", df.format(rpp.getTanggal()));
            //mapData.put("no_po", rpp.getNo_po().getNo_po());
            try {
                mapData.put("pegawai", "(" + rpp.getId_pegawai().getId_pegawai() + ") " + rpp.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {

            }
//            try {
//                mapData.put("supplier", "("+ rpp.getKode_supplier().getKode_supplier()+") "+ rpp.getKode_supplier().getNama_supplier());
//            } catch (Exception ex) {
//                
//            }
//            try {
//                mapData.put("barang", "("+ rpp.getKode_barang().getKode_barang()+") "+ rpp.getKode_barang().getNama_barang());
//            } catch (Exception ex) {
//                
//            }
            showData.add(mapData);
        }

        model.addAttribute("dataList", showData);
        session.close();
        return "returPembelianLaporan";
    }

    @RequestMapping(value = "retur-pembelian/po/{kode}", method = RequestMethod.GET)
    public String displayByPO(ModelMap model, @PathVariable(value = "kode") String kode) {
        //penjualanController penjualanController = new penjualanController();
        //model = penjualanController.getDataPenjualan(model, kode);
        model = getDataPembelianInRetur(model, kode);
        return "returpembelian2";
    }
    
    @RequestMapping(value = "retur-pembelian/laporan/{kode}", method = RequestMethod.GET)
    public String displayReport(ModelMap model, @PathVariable(value = "kode") String kode) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria returPembelianCriteria = session.createCriteria(retur_pembelian.class);
        returPembelianCriteria.add(Restrictions.eq("no_returpembelian", kode));
        if(returPembelianCriteria.uniqueResult() != null)
        {
            retur_pembelian returPembelian = (retur_pembelian) returPembelianCriteria.uniqueResult();
            Map modelHere = new HashMap();
            modelHere.put("no_faktur", returPembelian.getNo_retur_pembelian());
            modelHere.put("tanggal", df.format(returPembelian.getTanggal()));
            modelHere.put("pelanggan", returPembelian.getId_pegawai().getNama_pegawai());
            modelHere.put("no_po", returPembelian.getNo_po());
            Criteria poCriteria = session.createCriteria(purchase_order.class);
            poCriteria.add(Restrictions.eq("no_po", returPembelian.getNo_po()));
            if(poCriteria.uniqueResult() != null)
            {
                purchase_order po = (purchase_order) poCriteria.uniqueResult();
                modelHere.put("supplier", po.getKode_supplier_inpo().getNama_supplier());
                //modelHere.put("no_po", po.getNo_po());
            }
            //modelHere.put("supplier", returPembelian.);
            List detailData = new ArrayList();
            for(retur_pembelian_detail rpd : returPembelian.getReturPenjualanDetail())
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
                "headerapps", "Detail Retur Pembelian");
        }
        session.close();
        return "returpembelianlaporan2";
    }

    public ModelMap getDataPembelianInRetur(ModelMap model, String kode) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(purchase_order.class
        );
        criteria.add(Restrictions.eq("no_po", kode));
        if (criteria.uniqueResult()
                != null) {
            purchase_order pj = (purchase_order) criteria.uniqueResult();
            Map modelHere = new HashMap();
            modelHere.put("no_faktur", pj.getNo_po());
            modelHere.put("tanggal", df.format(pj.getTanggal()));
            try {
                modelHere.put("pelanggan", "(" + pj.getKode_supplier_inpo().getKode_supplier() + ") " + pj.getKode_supplier_inpo().getNama_supplier());
            } catch (Exception ex) {

            }
            try {
                modelHere.put("pegawai", "(" + pj.getId_pegawai_inpo().getId_pegawai() + ") " + pj.getId_pegawai_inpo().getNama_pegawai());
            } catch (Exception ex) {

            }

            List<detail_purchase_order> ldpo = pj.getPo_detail();
            List detailData = new ArrayList();
            for (detail_purchase_order dpe : ldpo) {
                Map modelHere1 = new HashMap();
                modelHere1.put("kode_barang", dpe.getKode_barang().getKode_barang());
                modelHere1.put("nama_barang", dpe.getNama_barang());

                //modelHere1.put("harga", dpe.get());
                //modelHere1.put("diskon", dpe.get());
                //modelHere1.put("total", dpe.get());
                Criteria returCriteria = session.createCriteria(retur_pembelian.class);
                returCriteria.add(Restrictions.eq("no_po", pj.getNo_po()));

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
                        "SELECT rpd.jumlah FROM retur_pembelian_detail rpd, retur_pembelian rp "
                        + " WHERE rp.no_po = '" + pj.getNo_po() + "'"
                        + " and rp.no_returpembelian = rpd.no_returpembelian and "
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
                "headerapps", "Detail Nota Pembelian");
        return model;
    }

    @RequestMapping(value = "retur-pembelian/po/{kode}", method = RequestMethod.POST)
    public String DOdataAdd2(ModelMap model, HttpServletRequest request) {
        String no_faktur = request.getParameter("no_faktur");
        String tanggal = request.getParameter("tanggal");
        String pelanggan = request.getParameter("pelanggan");
        String pegawai = request.getParameter("pegawai");
        System.out.println("pegawai " + pegawai);
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
        System.out.println("pegawai " + pegawai);
        criteria.add(Restrictions.eq("id_pegawai", pegawai));
        if (criteria.uniqueResult() != null) {
            pegawaiIn = (pegawai) criteria.uniqueResult();
        }

//        supplier pelangganIn = new supplier();
//        criteria = session.createCriteria(pelanggan.class);
//        criteria.add(Restrictions.eq("kode_pelanggan", pelanggan));
//        if (criteria.uniqueResult() != null) {
//            pelangganIn = (pelanggan) criteria.uniqueResult();
//        }
        purchase_order penjualan = new purchase_order();
        criteria = session.createCriteria(purchase_order.class);
        criteria.add(Restrictions.eq("no_po", no_faktur));
        if (criteria.uniqueResult() != null) {
            penjualan = (purchase_order) criteria.uniqueResult();
        }

        Transaction trx = session.beginTransaction();

        retur_pembelian pj = new retur_pembelian();
        //pj.setNo_returpenjualan("RJAL-0011");
        pj.setNo_po(penjualan.getNo_po());
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
                    && jumlah[i].length() > 0 //&& harga[i].length() > 0
                    //&& total[i].length() > 0
                    ) {
                String jmlBarang = jumlah[i].replace(".", "");
                try {
                    int jmlBarangl = Integer.valueOf(jmlBarang);
                    System.out.println("jumlah barang " + jmlBarangl + " " + pj.getNo_po());
                    if (jmlBarangl > 0) {
                        barang barangIn = new barang();
                        criteria = session.createCriteria(barang.class);
                        criteria.add(Restrictions.eq("kode_barang", kodeparameter[i]));
                        if (criteria.uniqueResult() != null) {
                            barangIn = (barang) criteria.uniqueResult();
                        }

                        Criteria returPenjualanCriteria = session.createCriteria(retur_pembelian.class);
                        returPenjualanCriteria.add(Restrictions.eq("no_po", no_faktur));
                        if (returPenjualanCriteria.uniqueResult() == null) // tidak ada di database
                        {
                             System.out.println("lengthdata nulllll ");
                            Criteria tem = session.createCriteria(retur_pembelian.class).setProjection(Projections.property("id"));
                             System.out.println("lengthdata nulllll 2");
                            criteria.addOrder(Order.desc("id"));
                             System.out.println("lengthdata nulllll 3");
                            criteria.setMaxResults(1);
                             System.out.println("lengthdata nulllll 4");

                            final String monthYear = Util.convertTime(System.currentTimeMillis());
                            final String prefix = "RB" + monthYear + "-";
                            String kodedata = prefix + "0001";
                             System.out.println("lengthdata nulllll 5");
                            if (tem.uniqueResult() != null) {
                                System.out.println("lengthdata nulllll 6");
                                kodedata = String.valueOf(Integer.valueOf(tem.uniqueResult().toString()) + 1);
                                
                                while (kodedata.length() < 4) {
                                    kodedata = "0" + kodedata;
                                }
                                kodedata = prefix + kodedata;
                                System.out.println("lengthdata nulllll 8 " + kodedata);
                            }
//                            String kodedata = "PO-0001";
//                            if (criteria.uniqueResult() != null) {
//                                kodedata = String.valueOf(Integer.valueOf(criteria.uniqueResult().toString()) + 1);
//                                while (kodedata.length() < 4) {
//                                    kodedata = "0" + kodedata;
//                                }
//                                kodedata = "PO-" + kodedata;
//                            }
//                            mapDAta.put("no_po", kodedata);
                            System.out.println("lengthdata nulllll " + kodedata);
                            pj.setNo_retur_pembelian(kodedata);
                            pj.setTanggal(new java.sql.Timestamp(new Date().getTime()));
                            //pj.setKode_supplier(null);

                            session.saveOrUpdate(pj);
                            retur_pembelian_detail returPenjualanDetail = new retur_pembelian_detail();
                            returPenjualanDetail.setJumlah(jmlBarangl);
                            returPenjualanDetail.setKode_barang(barangIn);
                            returPenjualanDetail.setReturPembelian(pj);
                            session.save(returPenjualanDetail);

                        } else //ada di database
                        {
                            System.out.println("lengthdata not null");
                            retur_pembelian rPenjualan = (retur_pembelian) returPenjualanCriteria.uniqueResult();
                            final String no_retur = rPenjualan.getNo_retur_pembelian();
                            Criteria returPenjualanDetailCriteria = session.createCriteria(retur_pembelian_detail.class);
                            returPenjualanDetailCriteria.
                                    add(Restrictions.eq("no_returpembelian.no_returpembelian", no_retur)).
                                    add(Restrictions.eq("kode_barang.id", barangIn.getId()));
                            System.out.println("bbbbbbbbb " + no_retur + "");

                            if (returPenjualanDetailCriteria.uniqueResult() != null) {
                                retur_pembelian_detail returPenjualanDetail = (retur_pembelian_detail) returPenjualanDetailCriteria.uniqueResult();

                                returPenjualanDetail.setJumlah(jmlBarangl);
                                returPenjualanDetail.setKode_barang(barangIn);
                                returPenjualanDetail.setReturPembelian(rPenjualan);
                                session.saveOrUpdate(returPenjualanDetail);
                            } else {
                                retur_pembelian_detail returPenjualanDetail = new retur_pembelian_detail();
                                returPenjualanDetail.setJumlah(jmlBarangl);
                                returPenjualanDetail.setKode_barang(barangIn);
                                returPenjualanDetail.setReturPembelian(rPenjualan);
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
}
