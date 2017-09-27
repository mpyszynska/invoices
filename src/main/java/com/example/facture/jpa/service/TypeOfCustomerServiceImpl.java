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

public class TypeOfCustomerServiceImpl implements TypeOfCustomerService {

    @Autowired
    private TypeOfCustomerDAO typeOfCustomerDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private MapperFacade mapperFacade;


    @Override
    public void saveTypeOfCustomer(TypeOfCustomerDTO typeOfCustomerDTO){
        TypeOfCustomer typeOfCustomer = mapperFacade.map(typeOfCustomerDTO, TypeOfCustomer.class);
        typeOfCustomerDAO.save(typeOfCustomer);
    }

    @Override
    public void updateTypeOfCustomer(TypeOfCustomerDTO typeOfCustomerDTO){
        TypeOfCustomer typeOfCustomer2 =typeOfCustomerDAO.getById(typeOfCustomerDTO.getId());
        typeOfCustomerDAO.update(typeOfCustomer2);
    }

    @Override
    public void deleteTypeOfCustomer(TypeOfCustomerDTO typeOfCustomerDTO){
        TypeOfCustomer typeOfCustomer = typeOfCustomerDAO.getById(typeOfCustomerDTO.getId());
        typeOfCustomerDAO.delete(typeOfCustomer);
    }

    @Override
    public TypeOfCustomer getTypeOfCustomerById(Long id){
        return typeOfCustomerDAO.getById(id);
    }
    @Override
    public TypeOfCustomerDTO getTypeOfCustomerByName(String name){
        return mapperFacade.map(typeOfCustomerDAO.getTypeOfCustomerByName(name), TypeOfCustomerDTO.class);
    }

    @Override
    public  List<TypeOfCustomerDTO> getAllTypeOfCustomers(){
        List<TypeOfCustomerDTO> typeOfCustomers = new ArrayList<>();
        typeOfCustomerDAO.getAll().forEach(typeOfCustomer -> typeOfCustomers.add(mapperFacade.map(typeOfCustomer, TypeOfCustomerDTO.class)));
        return typeOfCustomers;
    }

    public void addCustomer2TypeOfCustomer(TypeOfCustomer typeOfCustomer, Customer customer){
        if(customer.getTypeOfCustomer() != typeOfCustomer) {
            customer.setTypeOfCustomer(typeOfCustomer);
            customerDAO.update(customer);
        }
        if (!typeOfCustomerDAO.getCustomers(typeOfCustomer).contains(customer)){
            List<Customer> customers4 = typeOfCustomerDAO.getCustomers(typeOfCustomer);
            customers4.add(customer);
            typeOfCustomer.setCustomers(customers4);
            typeOfCustomerDAO.update(typeOfCustomer);

        }
    }

    @Override
    public void deleteCustomerFromTypeOfCustomer(TypeOfCustomer typeOfCustomer, Customer customer){
        if(customer.getTypeOfCustomer() == typeOfCustomer){
            customer.setTypeOfCustomer(null);
            customerDAO.update(customer);

        }
        if(typeOfCustomerDAO.getCustomers(typeOfCustomer).contains(customer)){

            List<Customer> customers6 = typeOfCustomerDAO.getCustomers(typeOfCustomer);
            customers6.remove(customer);
            typeOfCustomer.setCustomers(customers6);
            typeOfCustomerDAO.update(typeOfCustomer);
        }
    }

    @Override
    public List<Customer> getCustomers(TypeOfCustomer typeOfCustomer){
        return  typeOfCustomerDAO.getCustomers(typeOfCustomer);
    }

}