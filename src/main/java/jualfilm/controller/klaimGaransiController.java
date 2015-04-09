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
import modelDatabase.klaim_garansi;
import modelDatabase.pegawai;
import modelDatabase.pelanggan;
import modelDatabase.penjualan;
import modelDatabase.purchase_order;
import modelDatabase.retur_pembelian;
import modelDatabase.retur_penjualan;
import modelDatabase.supplier;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Criteria;
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
public class klaimGaransiController {
    @RequestMapping(value="klaimgaransi", method = RequestMethod.GET)
    public String dataList(ModelMap model) {      
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(klaim_garansi.class);
        List<klaim_garansi> lreturPenjualan = criteria.list();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        List showData = new ArrayList();
        for (klaim_garansi klaim : lreturPenjualan) {
            Map mapData = new HashMap();
            mapData.put("no_klaim", klaim.getNo_klaim());
            mapData.put("tanggal", df.format(klaim.getTanggal()));
            mapData.put("no_faktur", klaim.getNo_faktur().getNo_faktur());
            
            try {
                mapData.put("pelanggan", "("+ klaim.getKode_pelanggan().getKode_pelanggan()+") "+ klaim.getKode_pelanggan().getNama_pelanggan());
            } catch (Exception ex) {
                
            }
            try {
                mapData.put("barang", "("+ klaim.getKode_barang().getKode_barang()+") "+ klaim.getKode_barang().getNama_barang());
            } catch (Exception ex) {
                
            }
            mapData.put("jumlah", klaim.getJumlah());
            showData.add(mapData);
        }
        
        model.addAttribute("dataList", showData);
        session.close();
        return "klaimGaransiList";
    }
    
    @RequestMapping(value="klaimgaransi/delete", method = RequestMethod.GET)
    public String dataAdd(ModelMap model, HttpServletRequest request) {
        String kodesupplier = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(klaim_garansi.class);
        criteria.add(Restrictions.eq("no_klaim",kodesupplier));
        klaim_garansi rpp = (klaim_garansi) criteria.uniqueResult();
        Transaction trx = session.beginTransaction();
        session.delete(rpp);
        trx.commit();
        session.close();
        return "redirect:/klaimgaransi";
    }
    
    @RequestMapping(value="klaimgaransi/add", method = RequestMethod.GET)
    public String dataAdd(ModelMap model) {  
        model.addAttribute("headerapps", "Klaim Gransi Baru");
        
        Map mapDAta = new HashMap();
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(klaim_garansi.class).setProjection(Projections.property("id"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        String kodedata = "GAR-0001";
        if (criteria.uniqueResult() != null ) {
            kodedata = String.valueOf(Integer.valueOf(criteria.uniqueResult().toString())+1);
            while (kodedata.length() < 4) {
                kodedata = "0"+kodedata;
            }
            kodedata = "GAR-"+kodedata;
        }
        mapDAta.put("no_klaim", kodedata);
        model.addAttribute("dataEdit", mapDAta);
        session.close();
        
        return "klaimGaransiAdd";
    }
    
    @RequestMapping(value="klaimgaransi/add", method = RequestMethod.POST)
    public String DOdataAdd(ModelMap model, HttpServletRequest request ) {
        String no_klaim = request.getParameter("no_klaim");
        String tanggal = request.getParameter("tanggal");
        String no_faktur = request.getParameter("no_faktur");
        String jumlah = request.getParameter("jumlah");
        if (jumlah.length()>0) {
            jumlah = jumlah.replace(".", "");
        }
        
        if (no_faktur != null ) {
            if (no_faktur.length() > 0 ) {
                char ch = (char)34;
                String komponen = String.valueOf(ch);
                
                if (no_faktur.substring(0,1).equals("\"")) {
                    no_faktur = no_faktur.substring(1);
                }
                
                if (no_faktur.substring(no_faktur.length()-1).equals("\"")) {
                    no_faktur = no_faktur.substring(0,no_faktur.length()-1);
                }
                
            }
        }
        
        String pelanggan = request.getParameter("pelanggan");
        if (pelanggan != null ) {
            if (pelanggan.length() > 0 ) {
                pelanggan = pelanggan.substring((pelanggan.indexOf("(")+1),pelanggan.indexOf(")"));
            }
        }
        String kode_barang = request.getParameter("kode_barang");
        if (kode_barang != null ) {
            if (kode_barang.length() > 0 ) {
                kode_barang = kode_barang.substring((kode_barang.indexOf("(")+1),kode_barang.indexOf(")"));
            }
        }
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        
        pelanggan pelangganIn = new pelanggan();
        Criteria criteria = session.createCriteria(pelanggan.class);
        criteria.add(Restrictions.eq("kode_pelanggan", pelanggan));
        if ( criteria .uniqueResult() != null) {
        	pelangganIn = (pelanggan) criteria .uniqueResult();
        }
        
        barang barangIn = new barang();
        criteria = session.createCriteria(barang.class);
        criteria.add(Restrictions.eq("kode_barang", kode_barang));
        if ( criteria .uniqueResult() != null) {
            barangIn = (barang) criteria .uniqueResult();
        }
        
        penjualan penjualanIn = new penjualan();
        criteria = session.createCriteria(penjualan.class);
        criteria.add(Restrictions.eq("no_faktur", no_faktur));
        if ( criteria .uniqueResult() != null) {
        	penjualanIn = (penjualan) criteria .uniqueResult();
        }
        
        Transaction trx = session.beginTransaction();
        
        klaim_garansi kga = new klaim_garansi();
        kga.setKode_barang(barangIn);
        kga.setKode_pelanggan(pelangganIn);
        kga.setNo_faktur(penjualanIn);
        kga.setNo_klaim(no_klaim);
        try {
            kga.setJumlah(Long.valueOf(jumlah));
        } catch (Exception ex) {
            
        }
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            kga.setTanggal(new Timestamp(df.parse(tanggal).getTime()));
        } catch (Exception ex) {
            
        }
        
        session.save(kga);
        trx.commit();
        session.close();
        
        return "redirect:/klaimgaransi";
    }
    
    
    @RequestMapping(value="klaimgaransi/validation", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String ValidationdataAdd(ModelMap model, HttpServletRequest request ) {
        String msg = "";
        int cansaved = 1;
        JSONObject jobj = new JSONObject();
        String kodesupplier = request.getParameter("no_klaim");
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(klaim_garansi.class).setProjection(Projections.rowCount());
        if ( request.getParameter("no_klaim1") != null ) {
            criteria.add(Restrictions.ne("no_klaim", request.getParameter("no_klaim1").toString() ));
        }
        criteria.add(Restrictions.eq("no_klaim", kodesupplier ));
        
        if (Integer.valueOf(criteria.uniqueResult().toString()) > 0 ){
            cansaved = 0;
            msg = "Kode "+kodesupplier+" telah digunakan oleh Klaim Gransi lain";
        }
        try {
            jobj.put("msg", msg);
            jobj.put("cansaved", cansaved);
        } catch (Exception ex) {
            
        }
        session.close();
        return jobj.toString();
    }
    
    @RequestMapping(value="klaimgaransi/edit", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, HttpServletRequest request ) {
        String returndata = "redirect:/klaimgaransi";
        String kodesupplier = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(klaim_garansi.class);
        criteria.add(Restrictions.eq("no_klaim",kodesupplier));
        if (criteria.uniqueResult() != null) {
            klaim_garansi kge = (klaim_garansi) criteria.uniqueResult();
            Map modelData = new HashMap();
            modelData.put("no_klaim", kge.getNo_klaim());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            modelData.put("tanggal", df.format(kge.getTanggal()));
            modelData.put("no_faktur", kge.getNo_faktur().getNo_faktur());
            try {
                modelData.put("pelanggan", "("+ kge.getKode_pelanggan().getKode_pelanggan()+") "+ kge.getKode_pelanggan().getNama_pelanggan());
            } catch (Exception ex) {
                
            }
            try {
                modelData.put("kode_barang", "("+ kge.getKode_barang().getKode_barang()+") "+ kge.getKode_barang().getNama_barang());
            } catch (Exception ex) {
                System.out.println(" error "+ex.getMessage());
            }
            modelData.put("jumlah", kge.getJumlah());
            model.addAttribute("dataEdit", modelData);
            returndata = "klaimGaransiAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Edit Klaim Gransi");
        return returndata;
    }
    
    @RequestMapping(value="klaimgaransi/edit", method = RequestMethod.POST)
    public String DOdataEdit(ModelMap model, HttpServletRequest request ) {        
        String returndata = "redirect:/klaimgaransi";
        String no_klaim = request.getParameter("no_klaim");
        String no_klaim1 = request.getParameter("no_klaim1");
        String tanggal = request.getParameter("tanggal");
        String no_faktur = request.getParameter("no_faktur");
        String jumlah = request.getParameter("jumlah");
        if (jumlah.length()>0) {
            jumlah = jumlah.replace(".", "");
        }
        
        if (no_faktur != null ) {
            if (no_faktur.length() > 0 ) {
                char ch = (char)34;
                String komponen = String.valueOf(ch);
                
                if (no_faktur.substring(0,1).equals("\"")) {
                    no_faktur = no_faktur.substring(1);
                }
                
                if (no_faktur.substring(no_faktur.length()-1).equals("\"")) {
                    no_faktur = no_faktur.substring(0,no_faktur.length()-1);
                }
                
            }
        }
        
        
        String pelanggan = request.getParameter("pelanggan");
        if (pelanggan != null ) {
            if (pelanggan.length() > 0 ) {
                pelanggan = pelanggan.substring((pelanggan.indexOf("(")+1),pelanggan.indexOf(")"));
            }
        }
        String kode_barang = request.getParameter("kode_barang");
        if (kode_barang != null ) {
            if (kode_barang.length() > 0 ) {
                kode_barang = kode_barang.substring((kode_barang.indexOf("(")+1),kode_barang.indexOf(")"));
            }
        }
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(klaim_garansi.class);
        criteria.add(Restrictions.eq("no_klaim", no_klaim1 ));
        if (criteria.uniqueResult() != null) {        
            Transaction trx = session.beginTransaction();
            klaim_garansi kga = (klaim_garansi) criteria.uniqueResult();
            
            pelanggan pelangganIn = new pelanggan();
            criteria = session.createCriteria(pelanggan.class);
            criteria.add(Restrictions.eq("kode_pelanggan", pelanggan));
            if ( criteria .uniqueResult() != null) {
            	pelangganIn = (pelanggan) criteria .uniqueResult();
            }
            
            barang barangIn = new barang();
            criteria = session.createCriteria(barang.class);
            criteria.add(Restrictions.eq("kode_barang", kode_barang));
            if ( criteria .uniqueResult() != null) {
                barangIn = (barang) criteria .uniqueResult();
            }
            
            penjualan penjualanIn = new penjualan();
            criteria = session.createCriteria(penjualan.class);
            criteria.add(Restrictions.eq("no_faktur", no_faktur));
            if ( criteria .uniqueResult() != null) {
            	penjualanIn = (penjualan) criteria .uniqueResult();
            }
            
            kga.setKode_barang(barangIn);
            kga.setKode_pelanggan(pelangganIn);
            kga.setNo_faktur(penjualanIn);
            try {
                kga.setJumlah(Long.valueOf(jumlah));
            } catch (Exception ex) {

            }
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                kga.setTanggal(new Timestamp(df.parse(tanggal).getTime()));
            } catch (Exception ex) {
  
            }
            
            session.update(kga);
            
            if (!no_klaim1.equalsIgnoreCase(no_klaim)) {
                String sql = "update klaim_garansi set no_klaim=:kode where no_klaim=:kode1";
                session.createQuery(sql).setParameter("kode", no_klaim).setParameter("kode1", no_klaim1).executeUpdate();
            }
            trx.commit();
        }
        
        session.close();
        return returndata;
    }
    

    @RequestMapping(value="klaimgaransi/one-detail/{kode}", method = RequestMethod.GET)
    public String dataEditDetail(ModelMap model, HttpServletRequest request , @PathVariable String kode ) {
        String returndata = "redirect:/klaimgaransi";
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(klaim_garansi.class);
        criteria.add(Restrictions.eq("no_klaim",kode));
        if (criteria.uniqueResult() != null) {
            klaim_garansi kge = (klaim_garansi) criteria.uniqueResult();
            Map modelData = new HashMap();
            modelData.put("no_klaim", kge.getNo_klaim());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            modelData.put("tanggal", df.format(kge.getTanggal()));
            modelData.put("no_faktur", kge.getNo_faktur().getNo_faktur());
            try {
                modelData.put("pelanggan", "("+ kge.getKode_pelanggan().getKode_pelanggan()+") "+ kge.getKode_pelanggan().getNama_pelanggan());
            } catch (Exception ex) {
                
            }
            try {
                modelData.put("kode_barang", "("+ kge.getKode_barang().getKode_barang()+") "+ kge.getKode_barang().getNama_barang());
            } catch (Exception ex) {
                System.out.println(" error "+ex.getMessage());
            }
            modelData.put("jumlah", kge.getJumlah());
            model.addAttribute("dataEdit", modelData);
            model.addAttribute("isDetail", true);
            returndata = "klaimGaransiAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Detail Klaim Gransi");
        return returndata;
    }
}
