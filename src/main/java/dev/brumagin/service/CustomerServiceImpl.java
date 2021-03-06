package dev.brumagin.service;

import dev.brumagin.data.BankAccountDAO;
import dev.brumagin.data.BankAccountDAOPostgresImpl;
import dev.brumagin.data.CustomerDAO;
import dev.brumagin.data.CustomerDAOPostgresImpl;
import dev.brumagin.entity.BankAccount;
import dev.brumagin.entity.Customer;
import dev.brumagin.utility.LinkedList;
import dev.brumagin.utility.List;

public class CustomerServiceImpl implements CustomerService{

    CustomerDAO customerDAO = new CustomerDAOPostgresImpl();
    BankAccountDAO bankAccountDAO = new BankAccountDAOPostgresImpl();

    @Override
    public Customer createCustomer(String firstName, String lastName) {
        if(firstName.equals("")||lastName.equals(""))
            return null;

        for(int i = 0 ; i < firstName.length(); i++){
            if(Character.isDigit(firstName.charAt(i))){
                return null;
            }
        }
        for(int i = 0 ; i < lastName.length(); i++){
            if(Character.isDigit(lastName.charAt(i))){
                return null;
            }
        }
        Customer customer = new Customer(firstName,lastName);
        customer = customerDAO.createCustomer(customer);
        return customer;
    }

    @Override
    public boolean deleteCustomer(String customerId){
        return customerDAO.deleteCustomer(customerId);
    }

    @Override
    public boolean updateName(Customer customer,String firstName,String lastName) {
        if(firstName.equals("")||lastName.equals(""))
            return false;

        for(int i = 0 ; i < firstName.length(); i++){
            if(!Character.isAlphabetic(firstName.charAt(i))){
                return false;
            }
        }
        for(int i = 0 ; i < lastName.length(); i++){
            if(!Character.isAlphabetic(lastName.charAt(i))){
                return false;
            }
        }

        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customerDAO.updateCustomer(customer);
        return true;
    }

    @Override
    public Customer getCustomer(String customerId) {
        return customerDAO.getCustomerById(customerId);
    }

    public boolean registerNewAccount(Customer customer,String username, String currentPassword) {

        boolean passwordAtLeastOneSpecial = false;
        boolean passwordAtLeastOneCapital = false;
        boolean passwordAtLeastOneNumber = false;

        if(username.length()<8 || username.length()>=20) {
            return false;
        }
        if(currentPassword.length()<10 || currentPassword.length()>=20) {
            return false;
        }
        for(int i=0 ; i<username.length();i++) {
            if (!Character.isDigit(username.charAt(i)) && !Character.isLetter(username.charAt(i))) {
                return false;
            }
        }
        for(int i=0 ; i<currentPassword.length();i++) {
            if (Character.isDigit(currentPassword.charAt(i)))
               passwordAtLeastOneNumber = true;
            if (Character.isUpperCase(currentPassword.charAt(i)))
                passwordAtLeastOneCapital = true;
            if (!Character.isLetterOrDigit(currentPassword.charAt(i)))
                passwordAtLeastOneSpecial = true;
        }
        if(passwordAtLeastOneCapital && passwordAtLeastOneNumber && passwordAtLeastOneSpecial) {
            customer.setUsername(username);
            customer.setPassword(currentPassword);
            customerDAO.updateLogin(customer);
            return true;
        }

        return false;

    }

    @Override
    public boolean login(String username) {

        return customerDAO.getLogin(username);
    }

    @Override
    public Customer login(String username, String password) {
        return customerDAO.getCustomerById(customerDAO.getLogin(username,password));
    }



    @Override
    public Customer updatePassword(Customer customer, String newPassword) {
        boolean passwordAtLeastOneSpecial = false;
        boolean passwordAtLeastOneCapital = false;
        boolean passwordAtLeastOneNumber = false;
        if(newPassword.length()<10 || newPassword.length()>=20) {
            return null;
        }
        for(int i=0 ; i<newPassword.length();i++) {
            if (Character.isDigit(newPassword.charAt(i)))
                passwordAtLeastOneNumber = true;
            if (Character.isUpperCase(newPassword.charAt(i)))
                passwordAtLeastOneCapital = true;
            if (!Character.isDigit(newPassword.charAt(i)) || !Character.isLetter(newPassword.charAt(i)))
                passwordAtLeastOneSpecial = true;
        }
        if(passwordAtLeastOneCapital && passwordAtLeastOneNumber && passwordAtLeastOneSpecial) {
            customer.setPassword(newPassword);
            customerDAO.updateLogin(customer);
            customer.setPassword(newPassword);
            return customer;
        }
        return null;
    }

    @Override
    public void closeOnlineAccount(Customer customer) {
        customer.setUsername(null);
        customer.setPassword(null);
        customerDAO.updateLogin(customer);
    }
}
