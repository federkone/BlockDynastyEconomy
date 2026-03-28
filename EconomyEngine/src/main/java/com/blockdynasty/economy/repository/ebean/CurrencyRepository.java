/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
package com.blockdynasty.economy.repository.ebean;

import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyAlreadyExist;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;
import BlockDynasty.Economy.domain.persistence.Exceptions.RepositoryException;
import BlockDynasty.Economy.domain.persistence.entities.ICurrencyRepository;
import com.blockdynasty.economy.repository.ebean.Mappers.CurrencyMapper;
import com.blockdynasty.economy.repository.ebean.Models.CurrencyDb;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.stream.Collectors;

public class CurrencyRepository implements ICurrencyRepository {
    private io.ebean.Database database;

    public CurrencyRepository(io.ebean.Database database) {
        this.database = database;
    }

    @Override
    public List<ICurrency> findAll() {
        try {
            // En Ebean, findList() sobre la clase de la entidad es el equivalente a SELECT c FROM CurrencyDb c
            return database.find(CurrencyDb.class)
                    .findList()
                    .stream()
                    .map(CurrencyMapper::toDomain)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // No se requiere rollback manual para operaciones de consulta (SELECT)
            throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
        }
    }

    @Override
    public ICurrency findByUuid(String uuid) {
        try {
            // En Ebean, findOne() devuelve null si no hay resultados, eliminando el try-catch de NoResultException
            CurrencyDb entity = database.find(CurrencyDb.class)
                    .where()
                    .eq("uuid", uuid)
                    .findOne();

            if (entity == null) {
                throw new CurrencyNotFoundException("Currency no encontrado: " + uuid);
            }

            return CurrencyMapper.toDomain(entity);

        } catch (CurrencyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            // No se requiere rollback en consultas de lectura con Ebean
            throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
        }
    }

    @Override
    public ICurrency findDefaultCurrency() {
        try {
            CurrencyDb entity = database.find(CurrencyDb.class)
                    .where()
                    .eq("defaultCurrency", true)
                    .findOne();

            if (entity == null) {
                throw new CurrencyNotFoundException("Currency default no encontrado");
            }

            return CurrencyMapper.toDomain(entity);
        } catch (CurrencyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
        }
    }

    @Override
    public ICurrency findByName(String name) {
        try {
            CurrencyDb entity = database.find(CurrencyDb.class)
                    .where()
                    .or()
                    .eq("singular", name)
                    .eq("plural", name)
                    .endOr()
                    .findOne();

            if (entity == null) {
                throw new CurrencyNotFoundException("Currency no encontrado por nombre: " + name);
            }

            return CurrencyMapper.toDomain(entity);
        } catch (CurrencyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
        }
    }

    @Override
    public void save(ICurrency currency) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {
            CurrencyDb currencyDb = database.find(CurrencyDb.class)
                    .where()
                    .or()
                    .eq("uuid", currency.getUuid().toString())
                    .eq("plural", currency.getPlural())
                    .eq("singular", currency.getSingular())
                    .endOr()
                    .findOne();

            if (currencyDb == null) {
                throw new CurrencyNotFoundException("Currency no encontrado: " + currency.getUuid());
            }

            CurrencyMapper.update(currency, currencyDb);

            database.update(currencyDb);
            tx.commit();
        } catch (CurrencyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(ICurrency currency) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {
            CurrencyDb currencyDb = database.find(CurrencyDb.class)
                    .where()
                    .or()
                    .eq("uuid", currency.getUuid().toString())
                    .eq("plural", currency.getPlural())
                    .eq("singular", currency.getSingular())
                    .endOr()
                    .findOne();

            if (currencyDb == null) {
                throw new CurrencyNotFoundException("Currency no encontrado: " + currency.getUuid());
            }

            database.delete(currencyDb);
            tx.commit();
        } catch (CurrencyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(ICurrency currency) {
        try (io.ebean.Transaction tx = database.beginTransaction()) {
            CurrencyDb currencyDb = database.find(CurrencyDb.class)
                    .where()
                    .or()
                    .eq("uuid", currency.getUuid().toString())
                    .eq("plural", currency.getPlural())
                    .eq("singular", currency.getSingular())
                    .endOr()
                    .findOne();

            if (currencyDb == null) {
                throw new CurrencyNotFoundException("Currency no encontrado: " + currency.getUuid());
            }

            CurrencyMapper.update(currency, currencyDb);

            database.update(currencyDb);
            tx.commit();
        } catch (CurrencyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error repositorio: " + e.getMessage(), e);
        }
    }

    @Override
    public void create(ICurrency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency no puede ser nulo");
        }

        try (io.ebean.Transaction tx = database.beginTransaction()) {
            // Verificar existencia usando findCount() con condiciones OR
            int count = database.find(CurrencyDb.class)
                    .where()
                    .or()
                    .eq("uuid", currency.getUuid().toString())
                    .eq("singular", currency.getSingular())
                    .eq("plural", currency.getPlural())
                    .endOr()
                    .findCount();

            if (count > 0) {
                throw new CurrencyAlreadyExist("Ya existe una moneda con el mismo UUID, nombre singular o plural");
            }

            // Mapear y guardar
            CurrencyDb entity = CurrencyMapper.toEntity(currency);
            database.save(entity);

            tx.commit();
        } catch (CurrencyAlreadyExist e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Error al crear la moneda: " + e.getMessage(), e);
        }
    }
}
*/