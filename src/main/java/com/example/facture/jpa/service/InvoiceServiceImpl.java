package com.example.facture.jpa.service;

import com.example.facture.jpa.dao.*;
import com.example.facture.jpa.dto.*;
import com.example.facture.jpa.model.*;
import com.example.facture.jpa.service.Mapper;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional

public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceDAO invoiceDAO;

    @Autowired
    private InvoiceItemDAO invoiceItemDAO;

    @Autowired
    private AddressDAO addressDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private MapperFacade mapperFacade;


    @Override
    public void saveInvoice(InvoiceDTO invoiceDTO){
        Invoice invoice = mapperFacade.map(invoiceDTO, Invoice.class);
        addAddress2Invoice(invoice, addressDAO.getById(invoiceDTO.getIdAddress()));
        addCustomer2Invoice(invoice, customerDAO.getById(invoiceDTO.getIdCustomer()));
        invoiceDAO.save(invoice);
    }

    @Override
    public void updateInvoice(InvoiceDTO invoiceDTO){
        Invoice invoice2 =invoiceDAO.getById(invoiceDTO.getId());
        invoiceDAO.update(invoice2);
    }

    @Override
    public void deleteInvoice(InvoiceDTO invoiceDTO){
        Invoice invoice3 = invoiceDAO.getById(invoiceDTO.getId());
        invoiceDAO.delete(invoice3);
    }

    @Override
    public Invoice getInvoiceById(Long id){
        return invoiceDAO.getById(id);
    }

    @Override
    public List<Invoice> getInvoiceBySellingDate(Date sellingDate){
        return invoiceDAO.getInvoiceBySellingDate(sellingDate);
    }

    @Override
    public List<Invoice> getInvoiceByConfirmDate(Date confirmDate){
        return invoiceDAO.getInvoiceByConfirmDate(confirmDate);
    }

    @Override
    public List<Invoice> getInvoiceByInvoiceDate(Date invoiceDate){
        return invoiceDAO.getInvoiceByInvoiceDate(invoiceDate);
    }

    @Override
    public InvoiceDTO getInvoiceByNumber(String number){
        return mapperFacade.map(invoiceDAO.getInvoiceByNumber(number), InvoiceDTO.class);
    }

    @Override
    public InvoiceDisplayDTO getInvoiceDisplayByNumber(String number){
        return mapperFacade.map(invoiceDAO.getInvoiceByNumber(number), InvoiceDisplayDTO.class);
    }


    @Override
    public  List<InvoiceDTO> getAllInvoices(){
        List<InvoiceDTO> invoices = new ArrayList<>();
        invoiceDAO.getAll().forEach(invoice -> invoices.add(mapperFacade.map(invoice, InvoiceDTO.class)));
        return invoices;
    }

    public void addInvoiceItem2Invoice(Invoice invoice, InvoiceItem invoiceItem){
        if(invoiceItem.getInvoice() != invoice) {
            invoiceItem.setInvoice(invoice);
            invoiceItemDAO.update(invoiceItem);
        }
        if (!invoiceDAO.getInvoiceItems(invoice).contains(invoiceItem)){
            List<InvoiceItem> invoiceItems = invoiceDAO.getInvoiceItems(invoice);
            invoiceItems.add(invoiceItem);
            invoice.setInvoiceItems(invoiceItems);
            invoiceDAO.update(invoice);

        }
    }

    @Override
    public void deleteInvoiceItemFromInvoice(Invoice invoice, InvoiceItem invoiceItem){
        if(invoiceItem.getInvoice() == invoice){
            invoiceItem.setInvoice(null);
            invoiceItemDAO.update(invoiceItem);

        }
        if(invoiceDAO.getInvoiceItems(invoice).contains(invoiceItem)){

            List<InvoiceItem> invoiceItems2 = invoiceDAO.getInvoiceItems(invoice);
            invoiceItems2.remove(invoiceItem);
            invoice.setInvoiceItems(invoiceItems2);
            invoiceDAO.update(invoice);
        }
    }

    @Override
    public List<InvoiceItem> getInvoiceItems(Invoice invoice){
        return  invoiceDAO.getInvoiceItems(invoice);
    }



    @Override
    public List<Invoice> getInvoiceByIdAddress(Long idAddress) {
        return invoiceDAO.getInvoiceByIdAddress(idAddress);
    }

    @Override
    public List<Invoice> getInvoiceByIdCustomer(Long idCustomer){
        return invoiceDAO.getInvoiceByIdCustomer(idCustomer);
    }

    @Override
    public List<Invoice> getInvoices(long idAddress, long idCustomer) {
        return invoiceDAO.getInvoices(idAddress, idCustomer);
    }

    @Override
    public void addAddress2Invoice(Invoice invoice, Address address) {
        if (invoice.getAddress() != address) {
            invoice.setAddress(address);
            invoiceDAO.update(invoice);
        }
        if (!addressDAO.getInvoices(address).contains(invoice)) {
            List<Invoice> invoices = addressDAO.getInvoices(address);
            invoices.add(invoice);
            address.setInvoices(invoices);
            addressDAO.update(address);
        }
    }

    @Override
    public void deleteAddressFromInvoice(Invoice invoice, Address address) {
        if (invoice.getAddress() == address) {
            invoice.setAddress(null);
            invoiceDAO.update(invoice);
        }
        if (addressDAO.getInvoices(address).contains(invoice)) {
            List<Invoice> invoices = addressDAO.getInvoices(address);
            invoices.remove(invoice);
            address.setInvoices(invoices);
            addressDAO.update(address);
        }
    }

    @Override
    public void addCustomer2Invoice(Invoice invoice, Customer customer){
        if(invoice.getCustomer() != customer){
            invoice.setCustomer(customer);
            invoiceDAO.update(invoice);
        }
        if(!customerDAO.getInvoices(customer).contains(invoice)){
            List<Invoice> invoices = customerDAO.getInvoices(customer);
            invoices.add(invoice);
            customer.setInvoices(invoices);
            customerDAO.update(customer);
        }
    }

    @Override
    public void deleteCustomerFromInvoice(Invoice invoice,Customer customer) {
        if (invoice.getCustomer() == customer) {
            invoice.setCustomer(null);
            invoiceDAO.update(invoice);
        }
        if (customerDAO.getInvoices(customer).contains(invoice)) {
            List<Invoice> invoices = customerDAO.getInvoices(customer);
            invoices.remove(invoice);
            customer.setInvoices(invoices);
            customerDAO.update(customer);
        }
    }










}