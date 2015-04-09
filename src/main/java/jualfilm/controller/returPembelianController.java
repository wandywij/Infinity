/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jualfilm.controller;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import org.hibernate.criterion.Order;
/**
 *
 * @author ade
 */
@Controller
public class returPembelianController {
    @RequestMapping(value="retur-pembelian", method = RequestMethod.GET)
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
            mapData.put("no_po", rpp.getNo_po().getNo_po());
            try {
                mapData.put("pegawai", "("+ rpp.getId_pegawai().getId_pegawai()+") "+ rpp.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {
                
            }
            try {
                mapData.put("supplier", "("+ rpp.getKode_supplier().getKode_supplier()+") "+ rpp.getKode_supplier().getNama_supplier());
            } catch (Exception ex) {
                
            }
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
    
    @RequestMapping(value="retur-pembelian/delete", method = RequestMethod.GET)
    public String dataAdd(ModelMap model, HttpServletRequest request) {
        String kodesupplier = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class);
        criteria.add(Restrictions.eq("no_retur_pembelian",kodesupplier));
        retur_pembelian rpp = (retur_pembelian) criteria.uniqueResult();
        Transaction trx = session.beginTransaction();
        session.delete(rpp);
        trx.commit();
        session.close();
        return "redirect:/retur-pembelian";
    }
    
    @RequestMapping(value="retur-pembelian/add", method = RequestMethod.GET)
    public String dataAdd(ModelMap model) {  
        model.addAttribute("headerapps", "Retur Pembelian Baru");
        
        Map mapDAta = new HashMap();
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class).setProjection(Projections.property("id"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        String kodedata = "RPO-0001";
        if (criteria.uniqueResult() != null ) {
            kodedata = String.valueOf(Integer.valueOf(criteria.uniqueResult().toString())+1);
            while (kodedata.length() < 4) {
                kodedata = "0"+kodedata;
            }
            kodedata = "RPO-"+kodedata;
        }
        mapDAta.put("no_retur_pembelian", kodedata);
        model.addAttribute("dataEdit", mapDAta);
        session.close();
        
        return "returPembelianAdd";
    }
    
    @RequestMapping(value="retur-pembelian/add", method = RequestMethod.POST)
    public String DOdataAdd(ModelMap model, HttpServletRequest request ) {
        String no_retur_pembelian = request.getParameter("no_retur_pembelian");
        String tanggal = request.getParameter("tanggal");
        String no_po = request.getParameter("no_po");
        
        if (no_po != null ) {
            if (no_po.length() > 0 ) {
                char ch = (char)34;
                String komponen = String.valueOf(ch);
                
                if (no_po.substring(0,1).equals("\"")) {
                    no_po = no_po.substring(1);
                }
                
                if (no_po.substring(no_po.length()-1).equals("\"")) {
                    no_po = no_po.substring(0,no_po.length()-1);
                }
                
            }
        }
        
        String pegawai = request.getParameter("pegawai");
        if (pegawai != null ) {
            if (pegawai.length() > 0 ) {
                pegawai = pegawai.substring((pegawai.indexOf("(")+1),pegawai.indexOf(")"));
            }
        }
        
        String supplier = request.getParameter("supplier");
        if (supplier != null ) {
            if (supplier.length() > 0 ) {
                supplier = supplier.substring((supplier.indexOf("(")+1),supplier.indexOf(")"));
            }
        }
        String kode_barang = request.getParameter("kode_barang");
        if (kode_barang != null ) {
            if (kode_barang.length() > 0 ) {
                kode_barang = kode_barang.substring((kode_barang.indexOf("(")+1),kode_barang.indexOf(")"));
            }
        }
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        
        purchase_order purchase_orderIn = new purchase_order();
        Criteria criteria = session.createCriteria(purchase_order.class);
        criteria.add(Restrictions.eq("no_po", no_po));
        if ( criteria .uniqueResult() != null) {
            purchase_orderIn = (purchase_order) criteria .uniqueResult();
        }
        
        supplier supplierIn = new supplier();
        criteria = session.createCriteria(supplier.class);
        criteria.add(Restrictions.eq("kode_supplier", supplier));
        if ( criteria .uniqueResult() != null) {
            supplierIn = (supplier) criteria .uniqueResult();
        }
        
        pegawai pegawaiIn = new pegawai();
        criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.eq("id_pegawai", pegawai));
        if ( criteria .uniqueResult() != null) {
            pegawaiIn = (pegawai) criteria .uniqueResult();
        }
        
        barang barangIn = new barang();
        criteria = session.createCriteria(barang.class);
        criteria.add(Restrictions.eq("kode_barang", kode_barang));
        if ( criteria .uniqueResult() != null) {
            barangIn = (barang) criteria .uniqueResult();
        }
        
        Transaction trx = session.beginTransaction();
        
        retur_pembelian rpp = new retur_pembelian();
        rpp.setId_pegawai(pegawaiIn);
        rpp.setKode_barang(barangIn);
        rpp.setKode_supplier(supplierIn);
        rpp.setNo_po(purchase_orderIn);
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
    
    
    @RequestMapping(value="retur-pembelian/validation", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String ValidationdataAdd(ModelMap model, HttpServletRequest request ) {
        String msg = "";
        int cansaved = 1;
        JSONObject jobj = new JSONObject();
        String kodesupplier = request.getParameter("no_retur_pembelian");
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class).setProjection(Projections.rowCount());
        if ( request.getParameter("no_retur_pembelian1") != null ) {
            criteria.add(Restrictions.ne("no_retur_pembelian", request.getParameter("no_retur_pembelian1").toString() ));
        }
        criteria.add(Restrictions.eq("no_retur_pembelian", kodesupplier ));
        
        if (Integer.valueOf(criteria.uniqueResult().toString()) > 0 ){
            cansaved = 0;
            msg = "Kode "+kodesupplier+" telah digunakan oleh Retur Pembelian lain";
        }
        try {
            jobj.put("msg", msg);
            jobj.put("cansaved", cansaved);
        } catch (Exception ex) {
            
        }
        session.close();
        return jobj.toString();
    }
    
    @RequestMapping(value="retur-pembelian/edit", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, HttpServletRequest request ) {
        String returndata = "redirect:/retur-pembelian";
        String kodesupplier = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class);
        criteria.add(Restrictions.eq("no_retur_pembelian",kodesupplier));
        if (criteria.uniqueResult() != null) {
            retur_pembelian rpp = (retur_pembelian) criteria.uniqueResult();
            Map modelData = new HashMap();
            modelData.put("no_retur_pembelian", rpp.getNo_retur_pembelian());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            modelData.put("tanggal", df.format(rpp.getTanggal()));
            modelData.put("no_po", rpp.getNo_po().getNo_po());
            try {
                modelData.put("pegawai", "("+ rpp.getId_pegawai().getId_pegawai()+") "+ rpp.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {
                
            }
            try {
                modelData.put("supplier", "("+ rpp.getKode_supplier().getKode_supplier()+") "+ rpp.getKode_supplier().getNama_supplier());
            } catch (Exception ex) {
                
            }
            try {
                modelData.put("kode_barang", "("+ rpp.getKode_barang().getKode_barang()+") "+ rpp.getKode_barang().getNama_barang());
            } catch (Exception ex) {
                System.out.println(" error "+ex.getMessage());
            }
            model.addAttribute("dataEdit", modelData);
            returndata = "returPembelianAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Edit Retur Pembelian");
        return returndata;
    }
    
    @RequestMapping(value="retur-pembelian/edit", method = RequestMethod.POST)
    public String DOdataEdit(ModelMap model, HttpServletRequest request ) {        
        String returndata = "redirect:/retur-pembelian";
        String no_retur_pembelian = request.getParameter("no_retur_pembelian");
        String no_retur_pembelian1 = request.getParameter("no_retur_pembelian1");
        String tanggal = request.getParameter("tanggal");
        String no_po = request.getParameter("no_po");
        
        if (no_po != null ) {
            if (no_po.length() > 0 ) {
                char ch = (char)34;
                String komponen = String.valueOf(ch);
                
                if (no_po.substring(0,1).equals("\"")) {
                    no_po = no_po.substring(1);
                }
                
                if (no_po.substring(no_po.length()-1).equals("\"")) {
                    no_po = no_po.substring(0,no_po.length()-1);
                }
                
            }
        }
        String pegawai = request.getParameter("pegawai");
        if (pegawai != null ) {
            if (pegawai.length() > 0 ) {
                pegawai = pegawai.substring((pegawai.indexOf("(")+1),pegawai.indexOf(")"));
            }
        }
        String supplier = request.getParameter("supplier");
        if (supplier != null ) {
            if (supplier.length() > 0 ) {
                supplier = supplier.substring((supplier.indexOf("(")+1),supplier.indexOf(")"));
            }
        }
        
        
        String kode_barang = request.getParameter("kode_barang");
        if (kode_barang != null ) {
            if (kode_barang.length() > 0 ) {
                kode_barang = kode_barang.substring((kode_barang.indexOf("(")+1),kode_barang.indexOf(")"));
            }
        }
        
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        
        purchase_order purchase_orderIn = new purchase_order();
        Criteria criteria = session.createCriteria(purchase_order.class);
        criteria.add(Restrictions.eq("no_po", no_po));
        if ( criteria .uniqueResult() != null) {
            purchase_orderIn = (purchase_order) criteria .uniqueResult();
        }
        
        supplier supplierIn = new supplier();
        criteria = session.createCriteria(supplier.class);
        criteria.add(Restrictions.eq("kode_supplier", supplier));
        if ( criteria .uniqueResult() != null) {
            supplierIn = (supplier) criteria .uniqueResult();
        }
        
        pegawai pegawaiIn = new pegawai();
        criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.eq("id_pegawai", pegawai));
        if ( criteria .uniqueResult() != null) {
            pegawaiIn = (pegawai) criteria .uniqueResult();
        }
        
        barang barangIn = new barang();
        criteria = session.createCriteria(barang.class);
        criteria.add(Restrictions.eq("kode_barang", kode_barang));
        if ( criteria .uniqueResult() != null) {
            barangIn = (barang) criteria .uniqueResult();
        }
        
        criteria = session.createCriteria(retur_pembelian.class);
        criteria.add(Restrictions.eq("no_retur_pembelian", no_retur_pembelian1 ));
        if (criteria.uniqueResult() != null) {        
            Transaction trx = session.beginTransaction();
            retur_pembelian rpp = (retur_pembelian) criteria.uniqueResult();
            
            rpp.setId_pegawai(pegawaiIn);
            rpp.setKode_barang(barangIn);
            rpp.setKode_supplier(supplierIn);
            rpp.setNo_po(purchase_orderIn);
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
    
    @RequestMapping(value="retur-pembelian/one-detail/{kode}", method = RequestMethod.GET)
    public String dataEditDetail(ModelMap model, HttpServletRequest request, 
            @PathVariable("kode") String kode  ) {
        String returndata = "redirect:/retur-pembelian";
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class);
        criteria.add(Restrictions.eq("no_retur_pembelian",kode));
        if (criteria.uniqueResult() != null) {
            retur_pembelian rpp = (retur_pembelian) criteria.uniqueResult();
            Map modelData = new HashMap();
            modelData.put("no_retur_pembelian", rpp.getNo_retur_pembelian());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            modelData.put("tanggal", df.format(rpp.getTanggal()));
            modelData.put("no_po", rpp.getNo_po().getNo_po());
            try {
                modelData.put("pegawai", "("+ rpp.getId_pegawai().getId_pegawai()+") "+ rpp.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {
                
            }
            try {
                modelData.put("supplier", "("+ rpp.getKode_supplier().getKode_supplier()+") "+ rpp.getKode_supplier().getNama_supplier());
            } catch (Exception ex) {
                
            }
            try {
                modelData.put("kode_barang", "("+ rpp.getKode_barang().getKode_barang()+") "+ rpp.getKode_barang().getNama_barang());
            } catch (Exception ex) {
                System.out.println(" error "+ex.getMessage());
            }
            model.addAttribute("dataEdit", modelData);
            model.addAttribute("isDetail", true);
            returndata = "returPembelianAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Detail Retur Pembelian");
        
        return returndata;
    }
    
    @RequestMapping(value="retur-pembelian/laporan", method = RequestMethod.GET)
    public String dataList2(ModelMap model, HttpServletRequest req) {      
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(retur_pembelian.class);
        
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
        	
        	if (req.getParameter("from") != null && req.getParameter("to") != null) {
        		Timestamp starDate = new Timestamp( df.parse(req.getParameter("from")+" 00:00:00").getTime() );
        		Timestamp toDate = new Timestamp(df.parse(req.getParameter("to")+" 59:59:59").getTime());
        		criteria.add(Restrictions.between("tanggal", starDate, toDate));
        		model.addAttribute("startDate", req.getParameter("from"));
        		model.addAttribute("endDate", req.getParameter("to"));
        	}
        	
        } catch (Exception ex) {
        	System.out.println(" err dataLaporanList "+ex.getMessage());
        }
        List<retur_pembelian> lreturPembelian = criteria.list();
        List showData = new ArrayList();
        df = new SimpleDateFormat("dd/MM/yyyy");
        for (retur_pembelian rpp : lreturPembelian) {
            Map mapData = new HashMap();
            mapData.put("no_retur_pembelian", rpp.getNo_retur_pembelian());
            mapData.put("tanggal", df.format(rpp.getTanggal()));
            mapData.put("no_po", rpp.getNo_po().getNo_po());
            try {
                mapData.put("pegawai", "("+ rpp.getId_pegawai().getId_pegawai()+") "+ rpp.getId_pegawai().getNama_pegawai());
            } catch (Exception ex) {
                
            }
            try {
                mapData.put("supplier", "("+ rpp.getKode_supplier().getKode_supplier()+") "+ rpp.getKode_supplier().getNama_supplier());
            } catch (Exception ex) {
                
            }
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
}
