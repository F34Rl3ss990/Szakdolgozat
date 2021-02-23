package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.entity.ServiceReservation;
import com.EGEA1R.CarService.persistance.repository.interfaces.ServiceReservationRepository;
import com.EGEA1R.CarService.service.interfaces.ServiceReservationService;
import com.EGEA1R.CarService.web.DTO.ServiceReservationDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServiceReservationServiceImpl implements ServiceReservationService {

    private ServiceReservationRepository serviceReservationRepository;

    private ModelMapper modelMapper;

    @Autowired
    public void setServiceRepository(ServiceReservationRepository serviceReservationRepository){
        this.serviceReservationRepository = serviceReservationRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveService(ServiceReservationDTO serviceReservationDTO){
        String services  = UserServiceImpl.servicesListToString(serviceReservationDTO.getReservedService());

        serviceReservationRepository.saveService(mapDTOtoServiceReservation(serviceReservationDTO), services);
    }

    @Override
    public PagedListHolder<ServiceReservation> getServicesByUserOrderByDate(int page, int size, Long userId){
        List<ServiceReservation> services = serviceReservationRepository.getAllServiceByUser(userId);
        PagedListHolder<ServiceReservation> pageHolder = new PagedListHolder<>(services);
        pageHolder.setPage(page);
        pageHolder.setPageSize(size);
        return pageHolder;
    }

    @Override
    public PagedListHolder<ServiceReservation> getServicesTodayOrderByDate(int page, int size){
        LocalDate localDate = LocalDate.now();
        List<ServiceReservation> services = serviceReservationRepository.getAllServiceByTodayDate(localDate);
        PagedListHolder<ServiceReservation> pageHolder = new PagedListHolder<>(services);
        pageHolder.setPage(page);
        pageHolder.setPageSize(size);
        return pageHolder;
    }

    private ServiceReservation mapDTOtoServiceReservation(ServiceReservationDTO serviceReservationDTO){
        ServiceReservation serviceReservation = modelMapper.map(serviceReservationDTO, ServiceReservation.class);
        return serviceReservation;
    }

}
