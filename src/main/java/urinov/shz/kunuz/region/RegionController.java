package urinov.shz.kunuz.region;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import urinov.shz.kunuz.articleType.LanguageEnum;
import urinov.shz.kunuz.util.Result;


import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    // 1. Region create (ADMIN)
@PostMapping("/adm/create")
    public ResponseEntity<RegionResponseDto> createRegion(@Valid @RequestBody RegionCreateDTO regionDto) {
    RegionResponseDto regionResponseDTO = regionService.createRegion(regionDto);
        return ResponseEntity.ok(regionResponseDTO);
    }

    // 2. Region update (ADMIN)
    @PutMapping("/adm/update/{id}")
    public ResponseEntity<Result> updateRegion(@Valid @RequestBody RegionCreateDTO regionDto,
                                               @PathVariable("id") int id) {
        Result result = regionService.updateRegion(regionDto,id);
        return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(result);
    }

    // 3. Region list (ADMIN)
    @GetMapping("/adm/list")
    public ResponseEntity<List<RegionResponseDto>> getRegionList() {
        List<RegionResponseDto> regionDtoList=regionService.getRegionList();
        return ResponseEntity.status(HttpStatus.OK).body(regionDtoList);
    }

    // 4. Region delete (ADMIN)
    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<Result> deleteRegion( @PathVariable int id) {

        Result result = regionService.deleteRegion(id);
        return ResponseEntity.status(result.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(result);
    }

    // 5. Region By Lang
    @GetMapping("/lang")
    public ResponseEntity<List<RegionResponseDto>> getRegionByLang2(@RequestHeader(value = "Accept-Language",defaultValue = "UZ") LanguageEnum lang) {

        List<RegionResponseDto> regionLangDtoList=regionService.getRegionByLang(lang);
        return ResponseEntity.status(HttpStatus.OK).body(regionLangDtoList);
    }



}
