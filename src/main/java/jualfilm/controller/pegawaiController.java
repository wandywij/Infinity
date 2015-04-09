/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jualfilm.controller;


import modelDatabase.hibernateUtil;

import javax.servlet.http.HttpServletRequest;

import modelDatabase.pegawai;

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
public class pegawaiController {
    @RequestMapping(value="pegawai", method = RequestMethod.GET)
    public String dataList(ModelMap model) {      
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pegawai.class);
        model.addAttribute("dataList", criteria.list());
        session.close();
        return "pegawaiList";
    }
    
    @RequestMapping(value="pegawai/delete", method = RequestMethod.GET)
    public String dataAdd(ModelMap model, HttpServletRequest request) {
        String kodepegawai = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.eq("id_pegawai",kodepegawai));
        pegawai pegawai1 = (pegawai) criteria.uniqueResult();
        Transaction trx = session.beginTransaction();
        session.delete(pegawai1);
        trx.commit();
        session.close();
        return "redirect:/pegawai";
    }
    
    @RequestMapping(value="pegawai/add", method = RequestMethod.GET)
    public String dataAdd(ModelMap model) {  
        model.addAttribute("headerapps", "Pegawai Baru");
        
        Map mapDAta = new HashMap();
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pegawai.class).setProjection(Projections.property("id"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        String kodedata = "Emp-0001";
        if (criteria.uniqueResult() != null ) {
            kodedata = String.valueOf(Integer.valueOf(criteria.uniqueResult().toString())+1);
            while (kodedata.length() < 4) {
                kodedata = "0"+kodedata;
            }
            kodedata = "Emp-"+kodedata;
        }
        mapDAta.put("id_pegawai", kodedata);
        model.addAttribute("dataEdit", mapDAta);
        session.close();
        
        return "pegawaiAdd";
    }
    
    @RequestMapping(value="pegawai/add", method = RequestMethod.POST)
    public String DOdataAdd(ModelMap model, HttpServletRequest request ) {
        String kodepegawai = request.getParameter("id_pegawai");
        String nama = request.getParameter("nama");
        String alamat = request.getParameter("alamat");
        String telp = request.getParameter("telp");
        String email = request.getParameter("email");
        String divisi = request.getParameter("divisi");
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Transaction trx = session.beginTransaction();
        
        pegawai pegawai1 = new pegawai();
        pegawai1.setId_pegawai(kodepegawai);
        pegawai1.setNama_pegawai(nama);
        pegawai1.setAlamat_pegawai(alamat);
        pegawai1.setTelepon_pegawai(telp);
        pegawai1.setEmail_pegawai(email);
        pegawai1.setDivisi(divisi);
        
        session.save(pegawai1);
        trx.commit();
        session.close();
        
        return "redirect:/pegawai";
    }
    
    
    @RequestMapping(value="pegawai/validation", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String ValidationdataAdd(ModelMap model, HttpServletRequest request ) {
        String msg = "";
        int cansaved = 1;
        JSONObject jobj = new JSONObject();
        String kodepegawai = request.getParameter("id_pegawai");
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pegawai.class).setProjection(Projections.rowCount());
        if ( request.getParameter("id_pegawai1") != null ) {
            criteria.add(Restrictions.ne("id_pegawai", request.getParameter("id_pegawai1").toString() ));
        }
        criteria.add(Restrictions.eq("id_pegawai", kodepegawai ));
        
        if (Integer.valueOf(criteria.uniqueResult().toString()) > 0 ){
            cansaved = 0;
            msg = "Kode "+kodepegawai+" telah digunakan oleh pegawai lain";
        }
        try {
            jobj.put("msg", msg);
            jobj.put("cansaved", cansaved);
        } catch (Exception ex) {
            
        }
        session.close();
        return jobj.toString();
    }
    
    @RequestMapping(value="pegawai/edit", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, HttpServletRequest request ) {
        String returndata = "redirect:/pegawai";
        String kodepegawai = request.getParameter("kode");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.eq("id_pegawai",kodepegawai));
        if (criteria.uniqueResult() != null) {
            pegawai pegawai1 = (pegawai) criteria.uniqueResult();
            model.addAttribute("dataEdit", pegawai1);
            returndata = "pegawaiAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Edit Pegawai");
        return returndata;
    }
    
    @RequestMapping(value="pegawai/edit", method = RequestMethod.POST)
    public String DOdataEdit(ModelMap model, HttpServletRequest request ) {        
        String returndata = "redirect:/pegawai";
        String kodepegawai1 = request.getParameter("id_pegawai1");
        String kodepegawai = request.getParameter("id_pegawai");
        String nama = request.getParameter("nama");
        String alamat = request.getParameter("alamat");
        String telp = request.getParameter("telp");
        String email = request.getParameter("email");
        String divisi = request.getParameter("divisi");
        
        
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.eq("id_pegawai", kodepegawai1 ));
        if (criteria.uniqueResult() != null) {        
            Transaction trx = session.beginTransaction();
            pegawai pegawai1 = (pegawai) criteria.uniqueResult();
            
            pegawai1.setNama_pegawai(nama);
            pegawai1.setAlamat_pegawai(alamat);
            pegawai1.setTelepon_pegawai(telp);
            pegawai1.setEmail_pegawai(email);
            pegawai1.setDivisi(divisi);
            pegawai1.setId_pegawai(kodepegawai);
            
            session.update(pegawai1);
            trx.commit();
        }
        
        session.close();
        return returndata;
    }
    
    @RequestMapping(value="pegawai/one-detail/{kode}", method = RequestMethod.GET)
    public String dataEdit(ModelMap model, HttpServletRequest request , @PathVariable String kode ) {
        String returndata = "redirect:/pegawai";
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.eq("id_pegawai",kode));
        if (criteria.uniqueResult() != null) {
            pegawai pegawai1 = (pegawai) criteria.uniqueResult();
            model.addAttribute("dataEdit", pegawai1);
            model.addAttribute("isDetail", true);
            returndata = "pegawaiAdd";
        }
        session.close();
        model.addAttribute("headerapps", "Detail Pegawai");
        return returndata;
    }
}
