/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jualfilm.controller;



import javax.servlet.http.HttpServletRequest;

import modelDatabase.hibernateUtil;
import modelDatabase.pelanggan;

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
public class pelangganController {
    @RequestMapping(value="pelanggan", method = RequestMethod.GET)
    public String dataList(ModelMap model) {      
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pelanggan.class);
        model.addAttribute("dataList", criteria.list());
        session.close();
        return "pelangganList";
    }
    
    @RequestMapping(value="pelanggan/delete", method = RequestMethod.GET)
    public String dataAdd(ModelMap model, HttpServletRequest request) {
        String kodepelanggan = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pelanggan.class);
        criteria.add(Restrictions.eq("kode_pelanggan",kodepelanggan));
        pelanggan pelanggan1 = (pelanggan) criteria.uniqueResult();
        Transaction trx = session.beginTransaction();
        session.delete(pelanggan1);
        trx.commit();
        session.close();
        return "redirect:/pelanggan";
    }
    
    @RequestMapping(value="pelanggan/add", method = RequestMethod.GET)
    public String dataAdd(ModelMap model) {
        Map mapDAta = new HashMap();
        model.addAttribute("headerapps", "Pelanggan Baru");
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pelanggan.class).setProjection(Projections.property("id"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        String kodedata = "Cust-0001";
        if (criteria.uniqueResult() != null ) {
            kodedata = String.valueOf(Integer.valueOf(criteria.uniqueResult().toString())+1);
            while (kodedata.length() < 4) {
                kodedata = "0"+kodedata;
            }
            kodedata = "Cust-"+kodedata;
        }
        mapDAta.put("kode_pelanggan", kodedata);
        model.addAttribute("dataEdit", mapDAta);
        session.close();
        
        return "pelangganAdd";
    }
    
    @RequestMapping(value="pelanggan/add", method = RequestMethod.POST)
    public String DOdataAdd(ModelMap model, HttpServletRequest request ) {
        String kodepelanggan = request.getParameter("nomor_pelanggan");
        String nama = request.getParameter("nama");
        String alamat = request.getParameter("alamat");
        String telp = request.getParameter("telp");
        String email = request.getParameter("email");
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction trx = session.beginTransaction();
        
        pelanggan pelanggan1 = new pelanggan();
        pelanggan1.setKode_pelanggan(kodepelanggan);
        pelanggan1.setNama_pelanggan(nama);
        pelanggan1.setAlamat_pelanggan(alamat);
        pelanggan1.setTelepon_pelanggan(telp);
        pelanggan1.setEmail_pelanggan(email);
        
        session.save(pelanggan1);
        trx.commit();
        session.close();
        
        return "redirect:/pelanggan";
    }
    
    
    @RequestMapping(value="pelanggan/validation", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String ValidationdataAdd(ModelMap model, HttpServletRequest request ) {
        String msg = "";
        int cansaved = 1;
        JSONObject jobj = new JSONObject();
        String kodepelanggan = request.getParameter("nomor_pelanggan");
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pelanggan.class).setProjection(Projections.rowCount());
        if ( request.getParameter("nomor_pelanggan1") != null ) {
            criteria.add(Restrictions.ne("kode_pelanggan", request.getParameter("nomor_pelanggan1").toString() ));
        }
        criteria.add(Restrictions.eq("kode_pelanggan", kodepelanggan ));
        
        if (Integer.valueOf(criteria.uniqueResult().toString()) > 0 ){
            cansaved = 0;
            msg = "Kode "+kodepelanggan+" telah digunakan oleh pelanggan lain";
        }
        try {
            jobj.put("msg", msg);
            jobj.put("cansaved", cansaved);
        } catch (Exception ex) {
            
        }
        session.close();
        return jobj.toString();
    }
    
    @RequestMapping(value="pelanggan/edit", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, HttpServletRequest request ) {
        String returndata = "redirect:/pelanggan";
        String kodepelanggan = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pelanggan.class);
        criteria.add(Restrictions.eq("kode_pelanggan",kodepelanggan));
        if (criteria.uniqueResult() != null) {
            pelanggan pelanggan1 = (pelanggan) criteria.uniqueResult();
            model.addAttribute("dataEdit", pelanggan1);
            returndata = "pelangganAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Edit Pelanggan");
        return returndata;
    }
    
    @RequestMapping(value="pelanggan/edit", method = RequestMethod.POST)
    public String DOdataEdit(ModelMap model, HttpServletRequest request ) {        
        String returndata = "redirect:/pelanggan";
        String kodepelanggan1 = request.getParameter("nomor_pelanggan1");
        String kodepelanggan = request.getParameter("nomor_pelanggan");
        String nama = request.getParameter("nama");
        String alamat = request.getParameter("alamat");
        String telp = request.getParameter("telp");
        String email = request.getParameter("email");
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pelanggan.class);
        criteria.add(Restrictions.eq("kode_pelanggan", kodepelanggan1 ));
        if (criteria.uniqueResult() != null) {        
            Transaction trx = session.beginTransaction();
            pelanggan pelanggan1 = (pelanggan) criteria.uniqueResult();
            
            pelanggan1.setNama_pelanggan(nama);
            pelanggan1.setAlamat_pelanggan(alamat);
            pelanggan1.setTelepon_pelanggan(telp);
            pelanggan1.setEmail_pelanggan(email);
            
            session.update(pelanggan1);
            
            if (!kodepelanggan1.equalsIgnoreCase(kodepelanggan)) {
                String sql = "update pelanggan set kode_pelanggan=:kode where kode_pelanggan=:kode1";
                session.createQuery(sql).setParameter("kode", kodepelanggan)
                        .setParameter("kode1", kodepelanggan1).executeUpdate();
            }
            trx.commit();
        }
        
        session.close();
        return returndata;
    }
    
    @RequestMapping(value="pelanggan/one-detail/{kode}", method = RequestMethod.GET)
    public String dataEditdetail(ModelMap model, HttpServletRequest request, @PathVariable String kode ) {
        String returndata = "redirect:/pelanggan";
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pelanggan.class);
        criteria.add(Restrictions.eq("kode_pelanggan",kode));
        if (criteria.uniqueResult() != null) {
            pelanggan pelanggan1 = (pelanggan) criteria.uniqueResult();
            model.addAttribute("dataEdit", pelanggan1);
            model.addAttribute("isDetail", true);
            returndata = "pelangganAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Detail Pelanggan");
        return returndata;
    }
}
