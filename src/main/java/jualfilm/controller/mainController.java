/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jualfilm.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import modelDatabase.barang;
import modelDatabase.hibernateUtil;
import modelDatabase.pegawai;
import modelDatabase.pelanggan;
import modelDatabase.penjualan;
import modelDatabase.purchase_order;
import modelDatabase.supplier;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 *
 * @author ade
 */
@Controller
public class mainController {
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index() {              
        return "redirect:/penjualan";
    }
    
    @RequestMapping(value="supplier.json", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String autocompletesupplier(HttpServletRequest request ) {
        String q = request.getParameter("q");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(supplier.class);
        criteria.add(Restrictions.or( Restrictions.like("kode_supplier", q+"%").ignoreCase() 
                ,Restrictions.like("nama_supplier", q+"%").ignoreCase() ));
        JSONArray jsonArrayy = new JSONArray();
        try {
            List<supplier> lsuplier = criteria.list();
            for (supplier sp : lsuplier) {
                jsonArrayy.put("("+sp.getKode_supplier()+") "+ sp.getNama_supplier());
            }
        } catch (Exception ex) {
            
        }
        session.close();
        return jsonArrayy.toString();
    }
    
    @RequestMapping(value="pegawai.json", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String autocompletePegawai(HttpServletRequest request ) {
        String q = request.getParameter("q");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(pegawai.class);
        criteria.add(Restrictions.or( Restrictions.like("id_pegawai", q+"%").ignoreCase() 
                ,Restrictions.like("nama_pegawai", q+"%").ignoreCase() ));
        JSONArray jsonArrayy = new JSONArray();
        try {
            List<pegawai> lpegawai = criteria.list();
            for (pegawai pg : lpegawai) {
                jsonArrayy.put("("+pg.getId_pegawai()+") "+ pg.getNama_pegawai() );
            }
        } catch (Exception ex) {
            
        }
        session.close();
        return jsonArrayy.toString();
    }
    
    @RequestMapping(value="barang.json", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String autocompleteBarang(HttpServletRequest request ) {
        String q = request.getParameter("q");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(barang.class);
        criteria.add(Restrictions.or( Restrictions.like("kode_barang", q+"%").ignoreCase() 
                ,Restrictions.like("nama_barang", q+"%").ignoreCase() ));
        JSONArray jsonArrayy = new JSONArray();
        try {
            List<barang> lbarang = criteria.list();
            for (barang bg : lbarang) {
                jsonArrayy.put("("+bg.getKode_barang()+") "+ bg.getNama_barang()) ;
            }
        } catch (Exception ex) {
            
        }
        session.close();
        return jsonArrayy.toString();
    }
    
    @RequestMapping(value="po.json", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String autocompletePurchaseORder(HttpServletRequest request ) {
        String q = request.getParameter("q");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria( purchase_order.class);
        criteria.add(Restrictions.like("no_po", q+"%").ignoreCase());
        JSONArray jsonArrayy = new JSONArray();
        try {
            List<purchase_order> lbarang = criteria.list();
            for (purchase_order po : lbarang) {
                jsonArrayy.put(po.getNo_po()) ;
            }
        } catch (Exception ex) {
            
        }
        session.close();
        return jsonArrayy.toString();
    }
    
    @RequestMapping(value="pelanggan.json", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String autocompletePelangganORder(HttpServletRequest request ) {
        String q = request.getParameter("q");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria( pelanggan.class);        
        criteria.add(Restrictions.or( Restrictions.like("kode_pelanggan", q+"%").ignoreCase() 
                ,Restrictions.like("nama_pelanggan", q+"%").ignoreCase() ));
        JSONArray jsonArrayy = new JSONArray();
        try {
            List<pelanggan> lbarang = criteria.list();
            for (pelanggan pl : lbarang) {
                jsonArrayy.put("("+pl.getKode_pelanggan()+") "+ pl.getNama_pelanggan() );
            }
        } catch (Exception ex) {
            
        }
        session.close();
        return jsonArrayy.toString();
    }
    
    
    @RequestMapping(value="penjualan.json", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String autocompletePenjualanORder(HttpServletRequest request ) {
        String q = request.getParameter("q");
        Session session = hibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria( penjualan.class);        
        criteria.add(Restrictions.like("no_faktur", q+"%").ignoreCase());
        JSONArray jsonArrayy = new JSONArray();
        try {
            List<penjualan> lbarang = criteria.list();
            for (penjualan pl : lbarang) {
                jsonArrayy.put(pl.getNo_faktur());
            }
        } catch (Exception ex) {
            
        }
        session.close();
        return jsonArrayy.toString();
    }
}
