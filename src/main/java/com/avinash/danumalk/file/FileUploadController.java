package com.avinash.danumalk.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;


    @PostMapping(value ="/upload", consumes = "multipart/form-data")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        FileUploadResponse response = fileStorageService.saveFile(file);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/read")
    public ResponseEntity<byte[]> readFile(
            @RequestParam("fileUrl") String fileUrl
    ) {
        byte[] fileBytes = FileUtils.readFileFromLocation(fileUrl);
        if (fileBytes != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + fileUrl)
                    .body(fileBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
