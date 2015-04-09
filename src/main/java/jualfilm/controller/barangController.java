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

import javax.servlet.http.HttpServletRequest;

import modelDatabase.barang;
import modelDatabase.hibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *
 * @author ade
 */
@Controller
public class barangController {
    @RequestMapping(value="barang", method = RequestMethod.GET)
    public String dataList(ModelMap model) {      
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(barang.class);
        model.addAttribute("dataList", criteria.list());
        session.close();
        return "barangList";
    }
    
    @RequestMapping(value="barang/delete", method = RequestMethod.GET)
    public String dataAdd(ModelMap model, HttpServletRequest request) {
        String kodebarang = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(barang.class);
        criteria.add(Restrictions.eq("kode_barang",kodebarang));
        barang barang1 = (barang) criteria.uniqueResult();
        Transaction trx = session.beginTransaction();
        session.delete(barang1);
        trx.commit();
        session.close();
        return "redirect:/barang";
    }
    
    @RequestMapping(value="barang/add", method = RequestMethod.GET)
    public String dataAdd(ModelMap model) {  
        Map mapDAta = new HashMap();
        model.addAttribute("headerapps", "Barang Baru");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(barang.class).setProjection(Projections.property("id"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        String kodedata = "Item-0001";
        if (criteria.uniqueResult() != null ) {
            kodedata = String.valueOf(Integer.valueOf(criteria.uniqueResult().toString())+1);
            while (kodedata.length() < 4) {
                kodedata = "0"+kodedata;
            }
            kodedata = "Item-"+kodedata;
        }
        mapDAta.put("kode_barang", kodedata);
        model.addAttribute("dataEdit", mapDAta);
        session.close();
        return "barangAdd";
    }
    
    @RequestMapping(value="barang/add", method = RequestMethod.POST)
    public String DOdataAdd(ModelMap model, HttpServletRequest request ) {
        String kodebarang = request.getParameter("kode_barang");
        String nama = request.getParameter("nama");
        String harga = request.getParameter("harga");
//        String jumlah_stok = request.getParameter("jumlah_stok");
//        if (jumlah_stok.length()<1) {
//            jumlah_stok = "0";
//        } else {
//            jumlah_stok = jumlah_stok.replace(".", "");
//        }
        String jumlah_stok = "0";
        if (harga.length()<1) {
            harga = "0";
        } else {
            harga = harga.replace(".", "");
        }
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction trx = session.beginTransaction();
        
        barang barang1 = new barang();
        barang1.setKode_barang(kodebarang);
        barang1.setNama_barang(nama);
        barang1.setHarga(Long.valueOf(harga));
        barang1.setJumlah_stok(Long.valueOf(jumlah_stok));
        
        session.save(barang1);
        trx.commit();
        session.close();
        
        return "redirect:/barang";
    }
    
    
    @RequestMapping(value="barang/validation", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String ValidationdataAdd(ModelMap model, HttpServletRequest request ) {
        String msg = "";
        int cansaved = 1;
        JSONObject jobj = new JSONObject();
        String kodebarang = request.getParameter("kode_barang");
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(barang.class).setProjection(Projections.rowCount());
        if ( request.getParameter("kode_barang1") != null ) {
            criteria.add(Restrictions.ne("kode_barang", request.getParameter("kode_barang1").toString() ));
        }
        criteria.add(Restrictions.eq("kode_barang", kodebarang ));
        
        if (Integer.valueOf(criteria.uniqueResult().toString()) > 0 ){
            cansaved = 0;
            msg = "Kode "+kodebarang+" telah digunakan oleh barang lain";
        }
        try {
            jobj.put("msg", msg);
            jobj.put("cansaved", cansaved);
        } catch (Exception ex) {
            
        }
        session.close();
        return jobj.toString();
    }
    
    @RequestMapping(value="barang/edit", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, HttpServletRequest request ) {
        String returndata = "redirect:/barang";
        String kodebarang = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(barang.class);
        criteria.add(Restrictions.eq("kode_barang",kodebarang));
        if (criteria.uniqueResult() != null) {
            barang barang1 = (barang) criteria.uniqueResult();
            model.addAttribute("dataEdit", barang1);
            returndata = "barangAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Edit barang");
        return returndata;
    }
    
    @RequestMapping(value="barang/edit", method = RequestMethod.POST)
    public String DOdataEdit(ModelMap model, HttpServletRequest request ) {        
        String returndata = "redirect:/barang";
        String kodebarang = request.getParameter("kode_barang");
        String kodebarang1 = request.getParameter("kode_barang1");
        String nama = request.getParameter("nama");
        String harga = request.getParameter("harga");
        String jumlah_stok = request.getParameter("jumlah_stok");
        if (jumlah_stok.length()<1) {
            jumlah_stok = "0";
        } else {
            jumlah_stok = jumlah_stok.replace(".", "");
        }
        if (harga.length()<1) {
            harga = "0";
        } else {
            harga = harga.replace(".", "");
        }
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(barang.class);
        criteria.add(Restrictions.eq("kode_barang", kodebarang1 ));
        if (criteria.uniqueResult() != null) {        
            Transaction trx = session.beginTransaction();
            barang barang1 = (barang) criteria.uniqueResult();
                        
            barang1.setNama_barang(nama);
            barang1.setHarga(Long.valueOf(harga));
            barang1.setJumlah_stok(Long.valueOf(jumlah_stok));
            barang1.setKode_barang(kodebarang);
            
            session.update(barang1);
            trx.commit();
        }
        
        session.close();
        return returndata;
    }
    
    @RequestMapping(value="barang/laporan", method = RequestMethod.GET)
    public String laporanList(ModelMap model) {
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(barang.class);
        model.addAttribute("dataList", criteria.list());
        session.close();
        return "barangLaporan";
    }
    
        
//    @RequestMapping(value = "/images/{imageType}", method = RequestMethod.GET)
//    public @ResponseBody
//    ResponseEntity<byte[]> getImage(
//            @RequestParam(value = "id") String imageId,
//            @PathVariable(value = "imageType") String imageType) {
//
//     //Some processing logic
//        return new ResponseEntity<byte[]>(imageData, headers, HttpStatus.OK);
//    }
    
//    @RequestMapping(value="barang/detail/{kode}", method = RequestMethod.GET)
//    public @ResponseBody String laporanDetailList
//        (ModelMap model, @PathVariable String kode) {
//    	String returnn = "redirect:/barang/laporan";
//    	Session session = hibernateUtil.getSessionFactory().openSession();
//    	Criteria criteria = session.createCriteria(barang.class);
//    	criteria.add(Restrictions.eq("kode_barang", kode));
//    	if (criteria.uniqueResult() != null) {
//	    	barang barang1 = (barang) criteria.uniqueResult();
//	    	model.addAttribute("barang", barang1);
//	    	returnn = "barangLaporanDetail e";
//    	}
//    	
//    	String sql ="SELECT pu.no_po,de.jumlah, pu.tanggal, 1 as keterangan "
//    			+ "from detail_purchase_order de join barang ba on de.id_barang = ba.id join purchase_order pu on de.no_po = pu.id "
//    			+ "where ba.kode_barang=:kodee "
//    			+ "union "
//    			+ "SELECT pe.no_faktur, de.jumlah, pe.tanggal, 0 as keterangan "
//    			+ "from detail_penjualan de join barang ba on de.id_barang = ba.id join penjualan pe on de.no_faktur = pe.id "
//    			+ "where ba.kode_barang =:kodee";
//    	SQLQuery querydata =  (SQLQuery) session.createSQLQuery(sql).setString("kodee", kode);
//    	List<Object[]> ldata =  querydata.list();
//    	List dataShow = new ArrayList();
//    	for (Object[] obj : ldata) {
//    		Map modelldata = new HashMap();
//    		modelldata.put("kode_transaksi", obj[0].toString());
//    		System.out.println("   ooo "+obj[0].toString());
//    		modelldata.put("jumlah", obj[1]);
//    		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//    		modelldata.put("tanggal", sdf.format(obj[2]).toString());
//    		modelldata.put("warna", obj[3]);
//    		
//    		dataShow.add(modelldata);
//    	}
//    	
//    	model.put("trxList", dataShow);
//    	session.close();
//    	return returnn;
//    }
    
    @RequestMapping(value="barang/detail/{kode}", method = RequestMethod.GET)
    public String laporanDetailList(ModelMap model, @PathVariable(value="kode") String kode,
            HttpServletRequest req) {
    	String returnn = "redirect:/barang/laporan";
    	Session session = hibernateUtil.getSessionFactory().openSession();
    	Criteria criteria = session.createCriteria(barang.class);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat dNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromToPenjualan = "", fromToPO = "";
        try {
        	
        	if (req.getParameter("from") != null && req.getParameter("to") != null) {
        		Timestamp starDate = new Timestamp( df.parse(req.getParameter("from")+" 00:00:00").getTime() );
        		Timestamp toDate = new Timestamp(df.parse(req.getParameter("to")+" 59:59:59").getTime());
        		//criteria.add(Restrictions.between("tanggal", starDate, toDate));
                        fromToPenjualan = " and (pe.tanggal between " + "'" + 
                                dNew.format(starDate) + "' and '" + dNew.format(toDate) + "') ";
                        fromToPO = " and (pu.tanggal between " + "'" + 
                                dNew.format(starDate) + "' and '" + dNew.format(toDate) + "') ";
        		model.addAttribute("startDate", req.getParameter("from"));
        		model.addAttribute("endDate", req.getParameter("to"));
        	}
        	
        } catch (Exception ex) {
        	System.out.println(" err dataLaporanList "+ex.getMessage());
        }
    	criteria.add(Restrictions.eq("kode_barang", kode));
    	if (criteria.uniqueResult() != null) {
	    	barang barang1 = (barang) criteria.uniqueResult();
	    	model.addAttribute("barang", barang1);
	    	returnn = "barangLaporanDetail";
    	}
    	
    	String sql ="SELECT pu.no_po,de.jumlah, pu.tanggal, 1 as keterangan "
    			+ "from detail_purchase_order de join barang ba on de.id_barang = ba.id join purchase_order pu on de.no_po = pu.id "
    			+ "where ba.kode_barang=:kodee " + fromToPO 
    			+ "union "
    			+ "SELECT pe.no_faktur, de.jumlah, pe.tanggal, 0 as keterangan "
    			+ "from detail_penjualan de join barang ba on de.id_barang = ba.id join penjualan pe on de.no_faktur = pe.id "
    			+ "where ba.kode_barang =:kodee " + fromToPenjualan;
        System.out.println(sql);
    	SQLQuery querydata =  (SQLQuery) session.createSQLQuery(sql).setString("kodee", kode);
    	List<Object[]> ldata =  querydata.list();
    	List dataShow = new ArrayList();
    	for (Object[] obj : ldata) {
    		Map modelldata = new HashMap();
    		modelldata.put("kode_transaksi", obj[0].toString());
    		System.out.println("   ooo "+obj[0].toString());
    		modelldata.put("jumlah", obj[1]);
    		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    		modelldata.put("tanggal", sdf.format(obj[2]).toString());
    		modelldata.put("warna", obj[3]);
    		
    		dataShow.add(modelldata);
    	}
    	
    	model.put("trxList", dataShow);
    	session.close();
    	return returnn;
    }
    
    @RequestMapping(value="barang/one-detail/{kodebarang}", method = RequestMethod.GET)
    public String dataOneDetail(ModelMap model, HttpServletRequest request, 
            @PathVariable(value = "kode") String kodebarang ) {
        String returndata = "redirect:/barang";
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(barang.class);
        criteria.add(Restrictions.eq("kode_barang",kodebarang));
        if (criteria.uniqueResult() != null) {
            barang barang1 = (barang) criteria.uniqueResult();
            model.addAttribute("dataEdit", barang1);
            model.addAttribute("isDetail", true);
            returndata = "barangAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Detail barang");
        return returndata;
    }
}
