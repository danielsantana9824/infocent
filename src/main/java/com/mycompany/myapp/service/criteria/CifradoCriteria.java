package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Cifrado} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CifradoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cifrados?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CifradoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private StringFilter cifrado;

    private LocalDateFilter fechaVencimiento;

    private Boolean distinct;

    public CifradoCriteria() {}

    public CifradoCriteria(CifradoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.cifrado = other.cifrado == null ? null : other.cifrado.copy();
        this.fechaVencimiento = other.fechaVencimiento == null ? null : other.fechaVencimiento.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CifradoCriteria copy() {
        return new CifradoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            descripcion = new StringFilter();
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public StringFilter getCifrado() {
        return cifrado;
    }

    public StringFilter cifrado() {
        if (cifrado == null) {
            cifrado = new StringFilter();
        }
        return cifrado;
    }

    public void setCifrado(StringFilter cifrado) {
        this.cifrado = cifrado;
    }

    public LocalDateFilter getFechaVencimiento() {
        return fechaVencimiento;
    }

    public LocalDateFilter fechaVencimiento() {
        if (fechaVencimiento == null) {
            fechaVencimiento = new LocalDateFilter();
        }
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDateFilter fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CifradoCriteria that = (CifradoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(cifrado, that.cifrado) &&
            Objects.equals(fechaVencimiento, that.fechaVencimiento) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, cifrado, fechaVencimiento, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CifradoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (cifrado != null ? "cifrado=" + cifrado + ", " : "") +
            (fechaVencimiento != null ? "fechaVencimiento=" + fechaVencimiento + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
