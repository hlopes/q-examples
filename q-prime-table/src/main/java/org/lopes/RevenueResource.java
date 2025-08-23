package org.lopes;

import io.quarkus.panache.common.Sort;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/api/revenues")
public class RevenueResource {

  @GET
  public List<MonthlyRevenue> getAll() {
    return MonthlyRevenue.listAll(Sort.by("period"));
  }
}
