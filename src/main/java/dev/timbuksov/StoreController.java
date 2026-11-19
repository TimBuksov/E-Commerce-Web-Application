package dev.timbuksov;
import java.math.BigDecimal;import java.util.*;import jakarta.validation.Valid;import jakarta.validation.constraints.*;import org.springframework.http.HttpStatus;import org.springframework.web.bind.annotation.*;import org.springframework.web.server.ResponseStatusException;import org.springframework.transaction.annotation.Transactional;import com.fasterxml.jackson.databind.ObjectMapper;
@RestController @RequestMapping("/api") public class StoreController {
 private final ProductRepository products;private final OrderRepository orders;private final ObjectMapper mapper;
 StoreController(ProductRepository products,OrderRepository orders,ObjectMapper mapper){this.products=products;this.orders=orders;this.mapper=mapper;}
 record CartLine(@NotNull Long productId,@Min(1) int quantity){}
 record Checkout(@NotBlank String customerName,@Email @NotBlank String email,@NotEmpty List<@Valid CartLine> items){}
 @GetMapping("/products") public List<Product> list(@RequestParam(required=false) String search){return products.findAll().stream().filter(p->search==null||p.name.toLowerCase().contains(search.toLowerCase())).toList();}
 @PostMapping("/orders") @ResponseStatus(HttpStatus.CREATED) @Transactional public Map<String,Object> checkout(@Valid @RequestBody Checkout request)throws Exception{
  if(request.items().size()>40)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cart too large");
  BigDecimal total=BigDecimal.ZERO;List<Map<String,Object>> lines=new ArrayList<>();Set<Long> unique=new HashSet<>();
  for(CartLine line:request.items()){if(!unique.add(line.productId()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Duplicate product");
   Product product=products.findById(line.productId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unknown product"));
   if(product.stock<line.quantity())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Insufficient stock for "+product.name);
   total=total.add(product.price.multiply(BigDecimal.valueOf(line.quantity())));product.stock-=line.quantity();products.save(product);
   lines.add(Map.of("productId",product.id,"name",product.name,"quantity",line.quantity(),"unitPrice",product.price));}
  CustomerOrder order=new CustomerOrder();order.customerName=request.customerName();order.email=request.email();order.total=total;order.itemsJson=mapper.writeValueAsString(lines);orders.save(order);
  return Map.of("orderId",order.id,"total",total,"status","DEMO_ORDER_CREATED");
 }
}
