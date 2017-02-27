package com.utdallas.onlineshopping.action.taxdetails;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.TaxDetailsHibernateDAO;
import com.utdallas.onlineshopping.payload.response.taxdetails.StatesListResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.List;

public class GetAllStatesAction implements Action<StatesListResponse>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;

    @Inject
    public GetAllStatesAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public StatesListResponse invoke()
    {
        TaxDetailsHibernateDAO taxDetailsHibernateDAO = this.hibernateUtil.getTaxDetailsHibernateDAO();
        List<String> allStates = taxDetailsHibernateDAO.getAllStates();

        StatesListResponse statesListResponse = new StatesListResponse();
        statesListResponse.setStates( allStates );
        return statesListResponse;
    }
}
