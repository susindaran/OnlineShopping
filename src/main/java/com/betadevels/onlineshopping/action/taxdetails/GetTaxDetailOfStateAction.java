package com.betadevels.onlineshopping.action.taxdetails;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.TaxDetailsHibernateDAO;
import com.betadevels.onlineshopping.models.TaxDetails;
import com.betadevels.onlineshopping.payload.response.taxdetails.TaxDetailResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.modelmapper.ModelMapper;

public class GetTaxDetailOfStateAction implements Action<TaxDetailResponse>
{
	private final HibernateUtil hibernateUtil;
	private ModelMapper modelMapper;
	private String state;

	@Inject
	public GetTaxDetailOfStateAction( Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
	{
		this.hibernateUtil = hibernateUtilProvider.get();
		this.modelMapper = modelMapper;
	}

	public GetTaxDetailOfStateAction forState(String state)
	{
		this.state = state;
		return this;
	}

	@Override
	public TaxDetailResponse invoke()
	{
		TaxDetailsHibernateDAO taxDetailsHibernateDAO = this.hibernateUtil.getTaxDetailsHibernateDAO();
		TaxDetails taxDetails = taxDetailsHibernateDAO.findByState( state );
		return modelMapper.map( taxDetails, TaxDetailResponse.class );
	}
}
