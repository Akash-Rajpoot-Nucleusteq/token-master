package in.nucleusteq.plasma.mapper;

public interface Mapper {
    <T, U> U map(T source, Class<U> destinationType);
}