package guru.qa.homework;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class ArchiveFilesTest {
    @BeforeAll
    static void beforeAll() throws Exception {
        ClassLoader cl = ArchiveFilesTest.class.getClassLoader();
        try (
                InputStream stream = cl.getResourceAsStream("hw.zip");
                ZipArchiveInputStream archive = new ZipArchiveInputStream(stream)
        ) {
            ZipArchiveEntry archiveEntry;

            while ((archiveEntry = archive.getNextZipEntry()) != null) {
                File file = new File("src/test/resources/out/" + archiveEntry.getName());
                IOUtils.copy(archive, new FileOutputStream(file));
            }
        }
    }


    @Test
    void fileCheckTest() throws Exception {
        ClassLoader cl = ArchiveFilesTest.class.getClassLoader();
        try (
                InputStream stream = cl.getResourceAsStream("hw.zip");
                ZipArchiveInputStream archive = new ZipArchiveInputStream(stream)
        ) {
            ZipArchiveEntry archiveEntry;

            while ((archiveEntry = archive.getNextZipEntry()) != null) {
                if (archiveEntry.getName().endsWith(".pdf")) {
                    PDF content = new PDF(new File("src/test/resources/out/" + archiveEntry.getName()));
                    assertThat(content.author).contains("Sam Brannen");
                }
                if (archiveEntry.getName().endsWith(".csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(archive));
                    List<String[]> content = reader.readAll();
                    assertThat(content.get(1)[8]).contains("Youngstown");
                }

                if (archiveEntry.getName().endsWith(".xls")) {
                    XLS xls = new XLS(new File("src/test/resources/out/" + archiveEntry.getName()));
                    assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue()).contains("Dulce");
                }
            }
        }
    }

    @AfterAll
    static void afterAll() {
        File directory = new File("src/test/resources/out/");
        for (File file: Objects.requireNonNull(directory.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }
}
