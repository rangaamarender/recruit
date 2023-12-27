package com.lucid.core.azure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;

@Component
public class AzureBlobService {

	@Autowired
	BlobServiceClient blobServiceClient;


	public String upload(MultipartFile multipartFile, String fileName,EnumAzureContainers entity) throws IOException {
		BlobClient blob = getContainer(entity).getBlobClient(fileName);
		blob.upload(multipartFile.getInputStream(), multipartFile.getSize(), true);
		return multipartFile.getOriginalFilename();
	}

	public byte[] getFile(String fileName,EnumAzureContainers entity) throws URISyntaxException {
		BlobClient blob = getContainer(entity).getBlobClient(fileName);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		blob.downloadStream(outputStream);
		final byte[] bytes = outputStream.toByteArray();
		return bytes;

	}

	public List<String> listBlobs(EnumAzureContainers entity) {
		PagedIterable<BlobItem> items = getContainer(entity).listBlobs();
		List<String> names = new ArrayList<String>();
		for (BlobItem item : items) {
			names.add(item.getName());
		}
		return names;

	}

	public Boolean deleteBlob(String blobName,EnumAzureContainers entity) {
		BlobClient blob = getContainer(entity).getBlobClient(blobName);
		blob.delete();
		return true;
	}

	private BlobContainerClient getContainer(EnumAzureContainers entity) {
		BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(entity.toString());
		return blobContainerClient;
	}

}
