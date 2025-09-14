package org.hlopes.dtos.response;

import org.hlopes.entity.Brand;
import org.hlopes.entity.Dealership;

import java.util.List;

public record FilterOptions(List<Brand> brands, List<Dealership> dealerships, List<String> colors,
                            List<String> features) {
}
