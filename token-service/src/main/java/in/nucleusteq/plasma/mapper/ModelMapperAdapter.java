package in.nucleusteq.plasma.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperAdapter implements Mapper{
    private final ModelMapper modelMapper;
    public ModelMapperAdapter() {
        this.modelMapper = new ModelMapper();
    }
    @Override
    public <T, U> U map(T source, Class<U> destinationType) {
        return modelMapper.map(source, destinationType);
    }
}
