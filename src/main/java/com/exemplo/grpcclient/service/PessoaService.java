package com.exemplo.grpcclient.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.exemplo.proto.FindByIdRequest;
import com.exemplo.proto.FindByIdResponse;
import com.exemplo.proto.PessoaServiceGrpc;
import com.exemplo.proto.PessoaServiceGrpc.PessoaServiceBlockingStub;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Service
public class PessoaService {

	private PessoaServiceBlockingStub stub;
	private ObjectMapper mapper = new ObjectMapper()
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);

	PessoaService() {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
				.usePlaintext()
				.build();

		stub = PessoaServiceGrpc.newBlockingStub(channel);

	}

	public Map<String, Object> findPessoaById(Long id) {
		FindByIdRequest request = FindByIdRequest.newBuilder()
				.setId(id)
				.build();
		final FindByIdResponse response = stub.findById(request);

		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", response.getId());
		map.put("nick", response.getApelido());
		map.put("nomeCompleto", String.format("%s %s", response.getNome(), response.getSobrenome()));

		final var animais = response.getAnimaisList().stream()
				.map(animal -> Map.of(
						"nome", animal.getNome(),
						"idade", animal.getIdade(),
						"racaId", animal.getRaca().getNumber()))
				.collect(Collectors.toList());
		map.put("animais", animais);
		map.put("isAlive", response.getVivo());
		return map;
	}

}
