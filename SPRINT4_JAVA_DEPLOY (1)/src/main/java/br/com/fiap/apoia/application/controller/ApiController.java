package br.com.fiap.apoia.application.controller;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import br.com.fiap.apoia.domain.model.Paciente;
import br.com.fiap.apoia.domain.model.Consulta;
import br.com.fiap.apoia.domain.service.ApoiaService;

import java.util.Map;

public class ApiController {

    private static final ApoiaService service = new ApoiaService();

    public static void routes() {
        ApiBuilder.path("/api", () -> {
            ApiBuilder.get("/", ctx -> ctx.json(Map.of("name","Apoia+ API","version","1.0.0")));
            ApiBuilder.path("/pacientes", () -> {
                ApiBuilder.get(ctx -> ctx.json(service.pacienteRepo().findAll()));
                ApiBuilder.post(ApiController::createPaciente);
                ApiBuilder.get("{id}", ApiController::getPaciente);
                ApiBuilder.put("{id}", ApiController::updatePaciente);
                ApiBuilder.delete("{id}", ApiController::deletePaciente);
            });
            ApiBuilder.path("/consultas", () -> {
                ApiBuilder.get(ctx -> ctx.json(service.consultaRepo().list()));
                ApiBuilder.post(ApiController::createConsulta);
                ApiBuilder.get("{id}", ApiController::getConsulta);
                ApiBuilder.put("{id}", ApiController::updateConsulta);
                ApiBuilder.delete("{id}", ApiController::deleteConsulta);
                ApiBuilder.post("{id}/confirmar", ApiController::confirmar);
                ApiBuilder.post("{id}/ausente", ApiController::ausente);
                ApiBuilder.post("{id}/lembretes", ApiController::gerarLembretes);
                ApiBuilder.get("{id}/lembretes", ApiController::listarLembretes);
            });
        });
    }

    private static void createPaciente(Context ctx) {
        Paciente p = ctx.bodyAsClass(Paciente.class);
        if (p.getNome() == null || p.getNome().isBlank() || p.getContato() == null) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("error","nome e contato são obrigatórios")); return;
        }
        if (p.getId() != null) p.setId(null);
        if (!p.isAtivo()) p.setAtivo(true);
        ctx.status(HttpStatus.CREATED).json(service.pacienteRepo().create(p));
    }

    private static void getPaciente(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        var opt = service.pacienteRepo().findById(id);
        if (opt.isEmpty()) { ctx.status(HttpStatus.NOT_FOUND); return; }
        ctx.json(opt.get());
    }

    private static void updatePaciente(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Paciente p = ctx.bodyAsClass(Paciente.class);
        if (!service.pacienteRepo().update(id, p)) ctx.status(HttpStatus.NOT_FOUND);
        else ctx.status(HttpStatus.NO_CONTENT);
    }

    private static void deletePaciente(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        if (!service.pacienteRepo().delete(id)) ctx.status(HttpStatus.NOT_FOUND);
        else ctx.status(HttpStatus.NO_CONTENT);
    }

    private static void createConsulta(Context ctx) {
        Consulta c = ctx.bodyAsClass(Consulta.class);
        if (c.getPacienteId() == null || c.getDataHora() == null || c.getLinkSala() == null) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("error","pacienteId, dataHora e linkSala são obrigatórios")); return;
        }
        if (c.getStatus() == null) c.setStatus("AGENDADA");
        ctx.status(HttpStatus.CREATED).json(service.consultaRepo().create(c));
    }

    private static void getConsulta(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        var opt = service.consultaRepo().find(id);
        if (opt.isEmpty()) { ctx.status(HttpStatus.NOT_FOUND); return; }
        ctx.json(opt.get());
    }

    private static void updateConsulta(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Consulta c = ctx.bodyAsClass(Consulta.class);
        if (!service.consultaRepo().update(id, c)) ctx.status(HttpStatus.NOT_FOUND);
        else ctx.status(HttpStatus.NO_CONTENT);
    }

    private static void deleteConsulta(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        if (!service.consultaRepo().delete(id)) ctx.status(HttpStatus.NOT_FOUND);
        else ctx.status(HttpStatus.NO_CONTENT);
    }

    private static void confirmar(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        ctx.json(service.confirmarPresenca(id));
    }

    private static void ausente(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        ctx.json(service.marcarAusente(id));
    }

    private static void gerarLembretes(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        ctx.json(service.gerarLembreteAutomatico(id));
    }

    private static void listarLembretes(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        ctx.json(service.lembreteRepo().listByConsulta(id));
    }
}
