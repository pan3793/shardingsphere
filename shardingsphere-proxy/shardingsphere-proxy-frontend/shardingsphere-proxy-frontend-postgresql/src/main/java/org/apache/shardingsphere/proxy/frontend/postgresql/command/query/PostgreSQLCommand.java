/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.proxy.frontend.postgresql.command.query;

import lombok.Getter;
import org.apache.shardingsphere.distsql.parser.statement.rdl.create.AddResourceStatement;
import org.apache.shardingsphere.sharding.distsql.parser.statement.CreateShardingTableRuleStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dal.AnalyzeTableStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dal.SetStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.AlterFunctionStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.AlterIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.AlterProcedureStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.AlterTableStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.AlterTablespaceStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.AlterViewStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.CreateDatabaseStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.CreateFunctionStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.CreateIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.CreateProcedureStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.CreateTableStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.CreateViewStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DropDatabaseStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DropFunctionStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DropIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DropProcedureStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DropTableStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DropTablespaceStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DropViewStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.TruncateStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.CallStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.DeleteStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.DoStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.InsertStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.SelectStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.UpdateStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.tcl.CommitStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.tcl.ReleaseSavepointStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.tcl.RollbackStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.tcl.RollbackToSavepointStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.tcl.SavepointStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.tcl.SetTransactionStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dal.PostgreSQLResetParameterStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dal.PostgreSQLVacuumStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.ddl.PostgreSQLAlterSequenceStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.ddl.PostgreSQLCreateSequenceStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.ddl.PostgreSQLCreateTablespaceStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.ddl.PostgreSQLDropSequenceStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.tcl.PostgreSQLBeginTransactionStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.tcl.PostgreSQLStartTransactionStatement;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PostgreSQL command.
 * @see <a href="https://www.postgresql.org/docs/13/sql-commands.html">SQL Commands</a>
 */
@Getter
public enum PostgreSQLCommand {
    
    SELECT(SelectStatement.class),
    INSERT(InsertStatement.class),
    UPDATE(UpdateStatement.class),
    DELETE(DeleteStatement.class),
    CALL(CallStatement.class),
    DO(DoStatement.class),
    ANALYZE(AnalyzeTableStatement.class),
    VACUUM(PostgreSQLVacuumStatement.class),
    ALTER_FUNCTION(AlterFunctionStatement.class),
    ALTER_INDEX(AlterIndexStatement.class),
    ALTER_PROCEDURE(AlterProcedureStatement.class),
    ALTER_SEQUENCE(PostgreSQLAlterSequenceStatement.class),
    ALTER_TABLESPACE(AlterTablespaceStatement.class),
    ALTER_TABLE(AlterTableStatement.class),
    ALTER_VIEW(AlterViewStatement.class),
    CREATE(AddResourceStatement.class, CreateShardingTableRuleStatement.class),
    CREATE_DATABASE(CreateDatabaseStatement.class),
    CREATE_FUNCTION(CreateFunctionStatement.class),
    CREATE_INDEX(CreateIndexStatement.class),
    CREATE_PROCEDURE(CreateProcedureStatement.class),
    CREATE_SEQUENCE(PostgreSQLCreateSequenceStatement.class),
    CREATE_TABLESPACE(PostgreSQLCreateTablespaceStatement.class),
    CREATE_TABLE(CreateTableStatement.class),
    CREATE_VIEW(CreateViewStatement.class),
    DROP_DATABASE(DropDatabaseStatement.class),
    DROP_FUNCTION(DropFunctionStatement.class),
    DROP_INDEX(DropIndexStatement.class),
    DROP_PROCEDURE(DropProcedureStatement.class),
    DROP_SEQUENCE(PostgreSQLDropSequenceStatement.class),
    DROP_TABLESPACE(DropTablespaceStatement.class),
    DROP_TABLE(DropTableStatement.class),
    DROP_VIEW(DropViewStatement.class),
    TRUNCATE_TABLE(TruncateStatement.class),
    BEGIN(PostgreSQLBeginTransactionStatement.class),
    START_TRANSACTION(PostgreSQLStartTransactionStatement.class),
    COMMIT(CommitStatement.class),
    SAVEPOINT(SavepointStatement.class),
    ROLLBACK(RollbackStatement.class, RollbackToSavepointStatement.class),
    RELEASE(ReleaseSavepointStatement.class),
    SET(SetStatement.class, SetTransactionStatement.class),
    RESET(PostgreSQLResetParameterStatement.class);
    
    private static final Map<Class<? extends SQLStatement>, Optional<PostgreSQLCommand>> COMPUTED_CLASSES = new ConcurrentHashMap<>(64, 1);
    
    private final Collection<Class<? extends SQLStatement>> sqlStatementClasses;
    
    private final String tag;
    
    @SafeVarargs
    PostgreSQLCommand(final Class<? extends SQLStatement>... sqlStatementClasses) {
        this.sqlStatementClasses = Arrays.asList(sqlStatementClasses);
        tag = name().replaceAll("_", " ");
    }
    
    /**
     * Value of PostgreSQL command via SQL statement class.
     * 
     * @param sqlStatementClass SQL statement class
     * @return PostgreSQL command
     */
    public static Optional<PostgreSQLCommand> valueOf(final Class<? extends SQLStatement> sqlStatementClass) {
        return COMPUTED_CLASSES.computeIfAbsent(sqlStatementClass, target -> Arrays.stream(PostgreSQLCommand.values()).filter(each -> matches(target, each)).findAny());
    }
    
    private static boolean matches(final Class<? extends SQLStatement> sqlStatementClass, final PostgreSQLCommand postgreSQLCommand) {
        return postgreSQLCommand.sqlStatementClasses.stream().anyMatch(each -> each.isAssignableFrom(sqlStatementClass));
    }
}
