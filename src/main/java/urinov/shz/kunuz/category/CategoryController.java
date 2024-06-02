package urinov.shz.kunuz.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import urinov.shz.kunuz.articleType.LanguageEnum;
import urinov.shz.kunuz.util.Result;


import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // 1. Create category (ADMIN)
    @PostMapping("/adm/create")
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryCreateDTO categoryDto) {
        CategoryResponseDto result = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok().body(result);
    }

    // 2. Update category (ADMIN)
    @PutMapping("/adm/update/{id}")
    public ResponseEntity<Result> updateCategory(@PathVariable int id,
                                                 @RequestBody CategoryCreateDTO categoryDto) {
        Result result = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(result);
    }

    // 3. Category list (ADMIN)
    @GetMapping("/adm/list")
    public ResponseEntity<List<CategoryResponseDto>> getCategoryList() {
      List<CategoryResponseDto> categoryDtoList=categoryService.getCategoryList();
      return ResponseEntity.status(HttpStatus.OK).body(categoryDtoList);
    }

    // 4. Delete category (ADMIN)
    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<Result> deleteRegion(@PathVariable int id) {
        Result result = categoryService.deleteRegion(id);
        return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(result);
    }

    // 5. Category By Lang
    @GetMapping("/lang")
    public ResponseEntity<List<CategoryResponseDto>> getCategoryByLang(@RequestHeader(value = "Accept-Language") LanguageEnum lang) {
        List<CategoryResponseDto> categoryLangDtoList=categoryService.getCategoryByLang2(lang);
        return ResponseEntity.status(HttpStatus.OK).body(categoryLangDtoList);
    }


}
