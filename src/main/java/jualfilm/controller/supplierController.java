/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jualfilm.controller;


import modelDatabase.hibernateUtil;

import javax.servlet.http.HttpServletRequest;

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
public class supplierController {
    @RequestMapping(value="supplier", method = RequestMethod.GET)
    public String dataList(ModelMap model) {      
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(supplier.class);
        model.addAttribute("dataList", criteria.list());
        session.close();
        return "supplierList";
    }
    
    @RequestMapping(value="supplier/delete", method = RequestMethod.GET)
    public String dataAdd(ModelMap model, HttpServletRequest request) {
        String kodesupplier = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(supplier.class);
        criteria.add(Restrictions.eq("kode_supplier",kodesupplier));
        supplier supplier1 = (supplier) criteria.uniqueResult();
        Transaction trx = session.beginTransaction();
        session.delete(supplier1);
        trx.commit();
        session.close();
        return "redirect:/supplier";
    }
    
    @RequestMapping(value="supplier/add", method = RequestMethod.GET)
    public String dataAdd(ModelMap model) {  
        model.addAttribute("headerapps", "Supplier Baru");
        
        Map mapDAta = new HashMap();
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(supplier.class).setProjection(Projections.property("id"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        String kodedata = "Sup-0001";
        if (criteria.uniqueResult() != null ) {
            kodedata = String.valueOf(Integer.valueOf(criteria.uniqueResult().toString())+1);
            while (kodedata.length() < 4) {
                kodedata = "0"+kodedata;
            }
            kodedata = "Sup-"+kodedata;
        }
        mapDAta.put("kode_supplier", kodedata);
        model.addAttribute("dataEdit", mapDAta);
        session.close();
        
        return "supplierAdd";
    }
    
    @RequestMapping(value="supplier/add", method = RequestMethod.POST)
    public String DOdataAdd(ModelMap model, HttpServletRequest request ) {
        String kodesupplier = request.getParameter("nomor_supplier");
        String nama = request.getParameter("nama");
        String alamat = request.getParameter("alamat");
        String telp = request.getParameter("telp");
        String email = request.getParameter("email");
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction trx = session.beginTransaction();
        
        supplier supplier1 = new supplier();
        supplier1.setKode_supplier(kodesupplier);
        supplier1.setNama_supplier(nama);
        supplier1.setAlamat_supplier(alamat);
        supplier1.setTelepon_supplier(telp);
        supplier1.setEmail_supplier(email);
        
        session.save(supplier1);
        trx.commit();
        session.close();
        
        return "redirect:/supplier";
    }
    
    
    @RequestMapping(value="supplier/validation", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String ValidationdataAdd(ModelMap model, HttpServletRequest request ) {
        String msg = "";
        int cansaved = 1;
        JSONObject jobj = new JSONObject();
        String kodesupplier = request.getParameter("nomor_supplier");
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(supplier.class).setProjection(Projections.rowCount());
        if ( request.getParameter("nomor_supplier1") != null ) {
            criteria.add(Restrictions.ne("kode_supplier", request.getParameter("nomor_supplier1").toString() ));
        }
        criteria.add(Restrictions.eq("kode_supplier", kodesupplier ));
        
        if (Integer.valueOf(criteria.uniqueResult().toString()) > 0 ){
            cansaved = 0;
            msg = "Kode "+kodesupplier+" telah digunakan oleh supplier lain";
        }
        try {
            jobj.put("msg", msg);
            jobj.put("cansaved", cansaved);
        } catch (Exception ex) {
            
        }
        session.close();
        return jobj.toString();
    }
    
    @RequestMapping(value="supplier/edit", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, HttpServletRequest request ) {
        String returndata = "redirect:/supplier";
        String kodesupplier = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(supplier.class);
        criteria.add(Restrictions.eq("kode_supplier",kodesupplier));
        if (criteria.uniqueResult() != null) {
            supplier supplier1 = (supplier) criteria.uniqueResult();
            model.addAttribute("dataEdit", supplier1);
            returndata = "supplierAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Edit Supplier");
        return returndata;
    }
    
    @RequestMapping(value="supplier/edit", method = RequestMethod.POST)
    public String DOdataEdit(ModelMap model, HttpServletRequest request ) {        
        String returndata = "redirect:/supplier";
        String kodesupplier1 = request.getParameter("nomor_supplier1");
        String kodesupplier = request.getParameter("nomor_supplier");
        String nama = request.getParameter("nama");
        String alamat = request.getParameter("alamat");
        String telp = request.getParameter("telp");
        String email = request.getParameter("email");
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(supplier.class);
        criteria.add(Restrictions.eq("kode_supplier", kodesupplier1 ));
        if (criteria.uniqueResult() != null) {        
            Transaction trx = session.beginTransaction();
            supplier supplier1 = (supplier) criteria.uniqueResult();
            
            supplier1.setNama_supplier(nama);
            supplier1.setAlamat_supplier(alamat);
            supplier1.setTelepon_supplier(telp);
            supplier1.setEmail_supplier(email);
            
            session.update(supplier1);
            
            if (!kodesupplier1.equalsIgnoreCase(kodesupplier)) {
                String sql = "update supplier set kode_supplier=:kode where kode_supplier=:kode1";
                session.createQuery(sql).setParameter("kode", kodesupplier)
                        .setParameter("kode1", kodesupplier1).executeUpdate();
            }
            trx.commit();
        }
        
        session.close();
        return returndata;
    }
    
    @RequestMapping(value="supplier/one-detail/{kode}", method = RequestMethod.GET)
    public String dataEditDetail(ModelMap model, HttpServletRequest request , @PathVariable String kode) {
        String returndata = "redirect:/supplier";
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(supplier.class);
        criteria.add(Restrictions.eq("kode_supplier",kode));
        if (criteria.uniqueResult() != null) {
            supplier supplier1 = (supplier) criteria.uniqueResult();
            model.addAttribute("dataEdit", supplier1);
            model.addAttribute("isDetail", true);
            returndata = "supplierAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Detail Supplier");
        return returndata;
    }
}
