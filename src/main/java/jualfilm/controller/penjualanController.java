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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import modelDatabase.hibernateUtil;

import javax.servlet.http.HttpServletRequest;
import modelDatabase.Util;

import modelDatabase.barang;
import modelDatabase.detail_penjualan;
import modelDatabase.detail_purchase_order;
import modelDatabase.pegawai;
import modelDatabase.pelanggan;
import modelDatabase.penjualan;
import modelDatabase.purchase_order;
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
import org.json.JSONArray;
import org.springframework.web.bind.annotation.ResponseBody;
import org.json.JSONObject;
/**
 *
 * @author ade
 */
@Controller
public class penjualanController {
    @RequestMapping(value="penjualan", method = RequestMethod.GET)
    public String dataList(ModelMap model) {      
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(penjualan.class);
        List<penjualan> lData = criteria.list();
        List dataShow = new ArrayList();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        for (penjualan pj : lData) {
           Map<String, String> modelHere = new HashMap<String, String>();
           modelHere.put("no_faktur", pj.getNo_faktur());
           modelHere.put("tanggal", df.format(pj.getTanggal()));
           try {
            modelHere.put("pelanggan", "("+ pj.getKode_pelanggan().getKode_pelanggan()+") "+pj.getKode_pelanggan().getNama_pelanggan() );
           } catch (Exception ex) {
               
           }
           try {
            modelHere.put("pegawai", "("+ pj.getId_pegawai().getId_pegawai()+ ") " +pj.getId_pegawai().getNama_pegawai());
           } catch (Exception ex) {
               
           }
           dataShow.add(modelHere);
        }
        model.addAttribute("dataList", dataShow);
        session.close();
        return "penjualanList";
    }
    
    @RequestMapping(value="penjualan/delete", method = RequestMethod.GET)
    public String dataDelete(ModelMap model, HttpServletRequest request) {
        String kodebarang = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(penjualan.class);
        criteria.add(Restrictions.eq("no_faktur",kodebarang));
        try {
            penjualan pj = (penjualan) criteria.uniqueResult();
            Transaction trx = session.beginTransaction();
            try {
                List<detail_penjualan> ldpe = pj.getPenjualan_detail();
                for (detail_penjualan dpjj : ldpe) {
                        if (dpjj != null) {
                            session.delete(dpjj);
                        }
                }
            } catch (Exception ex) {
                    System.out.println("error bagian ini "+ex.getMessage());
            }
            session.delete(pj);
            trx.commit();
        } catch (Exception ex) {
            System.out.println(" error dataDelete "+ex.getMessage());
        }
        session.close();
        return "redirect:/penjualan";
    }
    
    @RequestMapping(value="penjualan/add", method = RequestMethod.GET)
    public String dataAdd(ModelMap model) {  
        model.addAttribute("headerapps", " Penjualan Baru");
        
        Map mapDAta = new HashMap();
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(penjualan.class).setProjection(Projections.property("id"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        final String monthYear = Util.convertTime(System.currentTimeMillis());
        final String prefix = "SO" + monthYear + "-";
        String kodedata = prefix + "0001";
        
        if (criteria.uniqueResult() != null ) {
            kodedata = String.valueOf(Integer.valueOf(criteria.uniqueResult().toString())+1);
            while (kodedata.length() < 4) {
                kodedata = "0"+kodedata;
            }
            kodedata = prefix + kodedata;
        }
        mapDAta.put("no_faktur", kodedata);
        model.addAttribute("dataEdit", mapDAta);
        session.close();
        
        return "penjualanAdd";
    }
    
    @RequestMapping(value="penjualan/add", method = RequestMethod.POST)
    public String DOdataAdd(ModelMap model, HttpServletRequest request ) {
        String no_faktur = request.getParameter("no_faktur");
        String tanggal = request.getParameter("tanggal");
        String pelanggan = request.getParameter("pelanggan");
        String pegawai = request.getParameter("pegawai");
        pegawai = pegawai.substring((pegawai.indexOf("(")+1),pegawai.indexOf(")"));
        pelanggan = pelanggan.substring((pelanggan.indexOf("(")+1),pelanggan.indexOf(")"));
        
        String[] kodeparameter =  request.getParameterValues("kodebarang");
        String[] namabarang =  request.getParameterValues("namabarang");
        String[] jumlah =  request.getParameterValues("jumlah");
        String[] harga =  request.getParameterValues("harga");
        String[] diskon =  request.getParameterValues("diskon");
        String[] total =  request.getParameterValues("total");
        int lengthDAta = kodeparameter.length;
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        
        pegawai pegawaiIn = new pegawai();
        Criteria criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.eq("id_pegawai", pegawai));
        if ( criteria .uniqueResult() != null) {
            pegawaiIn = (pegawai) criteria .uniqueResult();
        }
        
        pelanggan pelangganIn = new pelanggan();
        criteria = session.createCriteria(pelanggan.class);
        criteria.add(Restrictions.eq("kode_pelanggan", pelanggan));
        if ( criteria .uniqueResult() != null) {
        	pelangganIn = (pelanggan) criteria .uniqueResult();
        }
        
        Transaction trx = session.beginTransaction();
        
       penjualan pj = new penjualan();
       pj.setNo_faktur(no_faktur);
       DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            pj.setTanggal(new Timestamp(df.parse(tanggal).getTime()));
        } catch (ParseException ex) {
            Logger.getLogger(purchaseOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pj.setKode_pelanggan(pelangganIn);
        pj.setId_pegawai(pegawaiIn);
        session.save(pj);
                
        int i = 0;
        for (i = 0; i<lengthDAta; i++) {
            if (kodeparameter[i].length()>0
                    &&namabarang[i].length()>0
                    &&jumlah[i].length()>0
                    &&harga[i].length()>0
                    &&total[i].length()>0
                    ) {
                String jmlBarang = jumlah[i].replace(".", "");
                String hargaS = harga[i].replace(".", "");
                String diskonS = diskon[i].replace(".", "");
                String totalS = total[i].replace(".", "");
                try {
                    Long jmlBarangl = Long.valueOf(jmlBarang);
                    if (jmlBarangl>0) {
                        detail_penjualan dpe = new detail_penjualan();
                        dpe.setNo_faktur(pj);
                        
                        barang barangIn = new barang();
                        criteria = session.createCriteria(barang.class);
                        criteria.add(Restrictions.eq("kode_barang", kodeparameter[i]));
                        if ( criteria .uniqueResult() != null) {
                            barangIn = (barang) criteria .uniqueResult();
                        }
                        
                        
                        dpe.setKode_barang(barangIn);
                        dpe.setNama_barang(namabarang[i]);
                        dpe.setJumlah(jmlBarangl);
                        try {
                            dpe.setHarga(Long.valueOf(hargaS));
                        } catch (Exception ex) {
                            
                        }
                        try {
                            dpe.setDiskon(Integer.valueOf(diskonS));
                        } catch (Exception ex) {
                            
                        }
                        try {
                            dpe.setTotal(Long.valueOf(totalS));
                        } catch (Exception ex) {
                            
                        }
                        session.save(dpe);
                    }
                } catch (Exception ex) {
                    System.out.println(" error add po "+ex.getMessage());
                }
            }
        }

        trx.commit();
        session.close();
        
        return "redirect:/penjualan";
    }
    
    
    @RequestMapping(value="penjualan/validation", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String ValidationdataAdd(ModelMap model, HttpServletRequest request ) {
        String msg = "";
        int cansaved = 1;
        JSONObject jobj = new JSONObject();
        String kodebarang = request.getParameter("no_faktur");
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(penjualan.class).setProjection(Projections.rowCount());
        if ( request.getParameter("no_faktur1") != null ) {
            criteria.add(Restrictions.ne("no_faktur", request.getParameter("no_faktur1").toString() ));
        }
        criteria.add(Restrictions.eq("no_faktur", kodebarang ));
        
        if (Integer.valueOf(criteria.uniqueResult().toString()) > 0 ){
            cansaved = 0;
            msg = "Nomor Faktur "+kodebarang+" telah digunakan oleh barang lain";
        }
         
        try {
            jobj.put("msg", msg);
            jobj.put("cansaved", cansaved);
        } catch (Exception ex) {
            
        }
        session.close();
        return jobj.toString();
    }
    
    @RequestMapping(value="penjualan/edit", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, HttpServletRequest request ) {
        String returndata = "redirect:/penjualan";
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String kodebarang = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(penjualan.class);
        criteria.add(Restrictions.eq("no_faktur",kodebarang));
        if (criteria.uniqueResult() != null) {
            penjualan pj = (penjualan) criteria.uniqueResult();
            Map modelHere = new HashMap();
            modelHere.put("no_faktur", pj.getNo_faktur());
            modelHere.put("tanggal", df.format( pj.getTanggal()) );
            try {
            modelHere.put("pelanggan", "("+ pj.getKode_pelanggan().getKode_pelanggan()+") "+pj.getKode_pelanggan().getNama_pelanggan() );
           } catch (Exception ex) {
               
           }
           try {
            modelHere.put("pegawai", "("+ pj.getId_pegawai().getId_pegawai()+ ") " +pj.getId_pegawai().getNama_pegawai());
           } catch (Exception ex) {
               
           }
           
           List<detail_penjualan> ldpo = pj.getPenjualan_detail();
           List detailData = new ArrayList();
           for (detail_penjualan dpe : ldpo) {
               Map modelHere1 = new HashMap();
               modelHere1.put("kode_barang",dpe.getKode_barang().getKode_barang());
               modelHere1.put("nama_barang",dpe.getNama_barang());
               modelHere1.put("jumlah",dpe.getJumlah());
               modelHere1.put("harga",dpe.getHarga());
               modelHere1.put("diskon",dpe.getDiskon());
               modelHere1.put("total",dpe.getTotal());
               detailData.add(modelHere1);
           }
           modelHere.put("detailData", detailData);
            model.addAttribute("dataEdit", modelHere);
            returndata = "penjualanAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Edit Nota Penjualan");
        return returndata;
    }
    
    @RequestMapping(value="penjualan/edit", method = RequestMethod.POST)
    public String DOdataEdit(ModelMap model, HttpServletRequest request ) { 
        String returndata = "redirect:/penjualan";
        String no_faktur = request.getParameter("no_faktur");
        String no_faktur1 = request.getParameter("no_faktur1");
        String tanggal = request.getParameter("tanggal");
        String pelanggan = request.getParameter("pelanggan");
        String pegawai = request.getParameter("pegawai");
        pegawai = pegawai.substring((pegawai.indexOf("(")+1),pegawai.indexOf(")"));
        pelanggan = pelanggan.substring((pelanggan.indexOf("(")+1),pelanggan.indexOf(")"));
        
        String[] kodeparameter =  request.getParameterValues("kodebarang");
        String[] namabarang =  request.getParameterValues("namabarang");
        String[] jumlah =  request.getParameterValues("jumlah");
        String[] harga =  request.getParameterValues("harga");
        String[] diskon =  request.getParameterValues("diskon");
        String[] total =  request.getParameterValues("total");
        int lengthDAta = kodeparameter.length;
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(penjualan.class);
        criteria.add(Restrictions.eq("no_faktur", no_faktur1 ));
        if (criteria.uniqueResult() != null) {        
            Transaction trx = session.beginTransaction();
            penjualan pj = (penjualan) criteria.uniqueResult();
            
               pj.setNo_faktur(no_faktur);
               DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    pj.setTanggal(new Timestamp(df.parse(tanggal).getTime()));
                } catch (ParseException ex) {
                    Logger.getLogger(purchaseOrderController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                pegawai pegawaiIn = new pegawai();
                criteria = session.createCriteria(pegawai.class);
                criteria.add(Restrictions.eq("id_pegawai", pegawai));
                if ( criteria .uniqueResult() != null) {
                    pegawaiIn = (pegawai) criteria .uniqueResult();
                }
                
                pelanggan pelangganIn = new pelanggan();
                criteria = session.createCriteria(pelanggan.class);
                criteria.add(Restrictions.eq("kode_pelanggan", pelanggan));
                if ( criteria .uniqueResult() != null) {
                	pelangganIn = (pelanggan) criteria .uniqueResult();
                }
                
                pj.setKode_pelanggan(pelangganIn);
                pj.setId_pegawai(pegawaiIn);
                session.save(pj);
                
                if (!no_faktur.equalsIgnoreCase(no_faktur1)) {
                    String sql = "update penjualan set no_faktur=:kode where no_faktur=:kode1";
                    session.createQuery(sql).setParameter("kode", no_faktur)
                            .setParameter("kode1", no_faktur1).executeUpdate();
                }
                
                List<detail_penjualan> ldpe = pj.getPenjualan_detail();
                for (detail_penjualan dpe : ldpe) {
                    session.delete(dpe);
                }

                int i = 0;
                for (i = 0; i<lengthDAta; i++) {
                    if (kodeparameter[i].length()>0
                            &&namabarang[i].length()>0
                            &&jumlah[i].length()>0
                            &&harga[i].length()>0
                            &&total[i].length()>0
                            ) {
                        String jmlBarang = jumlah[i].replace(".", "");
                        String hargaS = harga[i].replace(".", "");
                        String diskonS = diskon[i].replace(".", "");
                        String totalS = total[i].replace(".", "");
                        try {
                            Long jmlBarangl = Long.valueOf(jmlBarang);
                            if (jmlBarangl>0) {
                                detail_penjualan dpe = new detail_penjualan();
                                dpe.setNo_faktur(pj);
                                
                                barang barangIn = new barang();
                                criteria = session.createCriteria(barang.class);
                                criteria.add(Restrictions.eq("kode_barang", kodeparameter[i]));
                                if ( criteria .uniqueResult() != null) {
                                    barangIn = (barang) criteria .uniqueResult();
                                }
                                
                                
                                dpe.setKode_barang(barangIn);
                                dpe.setNama_barang(namabarang[i]);
                                dpe.setJumlah(jmlBarangl);
                                try {
                                    dpe.setHarga(Long.valueOf(hargaS));
                                } catch (Exception ex) {

                                }
                                try {
                                    dpe.setDiskon(Integer.valueOf(diskonS));
                                } catch (Exception ex) {

                                }
                                try {
                                    dpe.setTotal(Long.valueOf(totalS));
                                } catch (Exception ex) {

                                }
                                session.save(dpe);
                            }
                        } catch (Exception ex) {
                            System.out.println(" error add po "+ex.getMessage());
                        }
                    }
                }
            trx.commit();
        }
        
        session.close();
        return returndata;
    }
    
    @RequestMapping(value="penjualan/laporan", method = RequestMethod.GET)
    public String laporanList(ModelMap model, HttpServletRequest req) {      
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(penjualan.class);
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
        
        List<penjualan> lData = criteria.list();
        List dataShow = new ArrayList();
        df = new SimpleDateFormat("dd/MM/yyyy");
        for (penjualan pj : lData) {
           Map<String, String> modelHere = new HashMap<String, String>();
           modelHere.put("no_faktur", pj.getNo_faktur());
           modelHere.put("tanggal", df.format(pj.getTanggal()));
           List<detail_penjualan> dps = pj.getPenjualan_detail();
           int jumlah = 0;
           for(detail_penjualan dp : dps) {
        	   jumlah += dp.getHarga() ;
           }
           modelHere.put("jumlah", "Rp. " + String.valueOf(jumlah));
           dataShow.add(modelHere);
        }
        model.addAttribute("dataList", dataShow);
        session.close();
        return "penjualanLaporan";
    }
    
    @RequestMapping(value="penjualan/laporan/detail/{kode}", method = RequestMethod.GET)
    public String laporanDetailList(ModelMap model, @PathVariable(value = "kode") String kode) {
    	model = getModel(model, kode);
        final String viewReturn = "redirect:/penjualan/laporan";
    	return viewReturn;
    }
    
    @RequestMapping(value = "penjualan2.json", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String autocompletePenjualan(HttpServletRequest request) {
        
        JSONArray jsonArrayy = new JSONArray();
        String q = request.getParameter("term");

        final String sql = "Select no_faktur from penjualan where no_faktur like " +
                "'%" +q + "%'";
        Session session = hibernateUtil.getSessionFactory().openSession();
        Query query  = session.createSQLQuery(sql);

        List<penjualan> result = (List<penjualan>) query.list();
        Iterator itr = result.iterator();
        List dataShow = new ArrayList();
        while (itr.hasNext()) {
            Object[] obj = (Object[]) itr.next();
            jsonArrayy.put(obj[0].toString());
        }
        session.close();
        return jsonArrayy.toString();
    }
    
    public ModelMap getModel(ModelMap model, String kode)
    {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	List dataShow = new ArrayList();
    	Session session = hibernateUtil.getSessionFactory().openSession();
    	String viewReturn = "redirect:/penjualan/laporan";
    	Criteria criteria = session.createCriteria(penjualan.class);
    	criteria.add(Restrictions.eq("no_faktur", kode));
    	if (criteria.uniqueResult() != null) {
    		penjualan pj = (penjualan) criteria.uniqueResult();
    		model.addAttribute("tanggal", df.format(pj.getTanggal()));
    		List<detail_penjualan> dpes = pj.getPenjualan_detail();
    		int total = 0;
    		for (detail_penjualan dpe : dpes) {
    			 Map<String, Object> modelHere = new HashMap<String, Object>();
    			 modelHere.put("kodeBarang", dpe.getKode_barang().getKode_barang());
    			 modelHere.put("namaBarang", dpe.getKode_barang().getNama_barang());
    			 modelHere.put("jumlah", dpe.getJumlah());
    			 modelHere.put("harga", dpe.getHarga());
    			 modelHere.put("diskon", dpe.getDiskon());
    			 modelHere.put("total", dpe.getTotal());
    			 total += dpe.getTotal();
    			 dataShow.add(modelHere);
			}
    		model.addAttribute("total", total);
    		model.addAttribute("dataList", dataShow);
    		
    	}
    	session.close();
        return model;
    }

    @RequestMapping(value="penjualan/one-detail/{kode}", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, /*HttpServletRequest request, */
            @PathVariable(value = "kode") String kode ) {
        String returndata = "redirect:/penjualan";       
        model = getDataPenjualan(model, kode);
        returndata = "penjualanAdd";
        return returndata;
    }
    
    public ModelMap getDataPenjualan(ModelMap model, String kode)
    {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(penjualan.class);
        criteria.add(Restrictions.eq("no_faktur",kode));
        if (criteria.uniqueResult() != null) {
            penjualan pj = (penjualan) criteria.uniqueResult();
            Map modelHere = new HashMap();
            modelHere.put("no_faktur", pj.getNo_faktur());
            modelHere.put("tanggal", df.format( pj.getTanggal()) );
            try {
            modelHere.put("pelanggan", "("+ pj.getKode_pelanggan().getKode_pelanggan()+") "+pj.getKode_pelanggan().getNama_pelanggan() );
           } catch (Exception ex) {
               
           }
           try {
            modelHere.put("pegawai", "("+ pj.getId_pegawai().getId_pegawai()+ ") " +pj.getId_pegawai().getNama_pegawai());
           } catch (Exception ex) {
               
           }
           
           List<detail_penjualan> ldpo = pj.getPenjualan_detail();
           List detailData = new ArrayList();
           for (detail_penjualan dpe : ldpo) {
               Map modelHere1 = new HashMap();
               modelHere1.put("kode_barang",dpe.getKode_barang().getKode_barang());
               modelHere1.put("nama_barang",dpe.getNama_barang());
               modelHere1.put("jumlah",dpe.getJumlah());
               modelHere1.put("harga",dpe.getHarga());
               modelHere1.put("diskon",dpe.getDiskon());
               modelHere1.put("total",dpe.getTotal());
               detailData.add(modelHere1);
           }
           modelHere.put("detailData", detailData);
	        model.addAttribute("dataEdit", modelHere);
	        model.addAttribute("isDetail", true);
	        
        }
        session.close();
        model.addAttribute("headerapps", "Detail Nota Penjualan");
        return model;
    }
}
