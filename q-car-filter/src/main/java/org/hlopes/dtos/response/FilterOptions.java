package org.hlopes.dtos.response;

import java.util.List;

import org.hlopes.entity.Brand;
import org.hlopes.entity.Dealership;

public record FilterOptions(List<Brand> brands, List<Dealership> dealerships, List<String> colors,
                            List<String> features) {
}
