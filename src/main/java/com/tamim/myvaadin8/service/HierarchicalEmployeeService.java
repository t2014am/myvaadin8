package com.tamim.myvaadin8.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tamim.myvaadin8.model.HierarchicalEmployee;

public class HierarchicalEmployeeService implements ServiceInterface<HierarchicalEmployee> {
	private final Logger logger = LogManager.getLogger(this.getClass());

	private String restEndpoint = "https://tamim-springboot.herokuapp.com/rest_api/hierarchical_employees/";

	@Override
	public Iterable<HierarchicalEmployee> findAll() {
		logger.info("findAll() called!");
		List<HierarchicalEmployee> listCar = null;

		try {
			Client client = Client.create();

			WebResource webResource = client.resource(restEndpoint);
			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);
			ObjectMapper objectMapper = new ObjectMapper();
			listCar = objectMapper.readValue(output, new TypeReference<List<HierarchicalEmployee>>() {
			});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listCar;
	}

	@Override
	public Optional<HierarchicalEmployee> findById(Long id) {
		HierarchicalEmployee hierarchicalEmployee = null;

		try {
			Client client = Client.create();

			WebResource webResource = client.resource(restEndpoint + id);
			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);
//			HierarchicalEmployee outputt = response.getEntity(HierarchicalEmployee.class);
//			JsonObject outputt = response.getEntity(JsonObject.class);
			ObjectMapper objectMapper = new ObjectMapper();
			hierarchicalEmployee = objectMapper.readValue(output, HierarchicalEmployee.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Optional.ofNullable(hierarchicalEmployee);
	}

	@Override
	public HierarchicalEmployee save(HierarchicalEmployee e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

//	private Iterable<HierarchicalEmployee> getAllDummy() {
//		Set<HierarchicalEmployee> employeePostions = new HashSet<>();
//		employeePostions.add(new HierarchicalEmployee(1L, "Tamim", "Asefi", 0L, new ArrayList(), "Male", 1L));
//		employeePostions.add(new HierarchicalEmployee(2L, "X", "Asefi", 1L, new ArrayList(), "Male", 1L));
//		employeePostions.add(new HierarchicalEmployee(3L, "Y", "Asefi", 1L, new ArrayList(), "Male", 1L));
////		new Employee(id, firstName, lastName, supervisorId, speciality, gender, positionId)
//
//		return employeePostions;
//
//	}

}
