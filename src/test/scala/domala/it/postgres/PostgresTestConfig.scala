package domala.it.postgres

import com.typesafe.config.ConfigFactory
import domala.it.IntegrationTestConfig
import org.seasar.doma.jdbc.dialect.PostgresDialect
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource

class PostgresTestConfig private (url: String, user: String, password: String)
    extends IntegrationTestConfig(
      dataSource = new LocalTransactionDataSource(url, user, password),
      new PostgresDialect,
      "org.postgresql.Driver"
    )

object PostgresTestConfig {
  private[this] val conf = ConfigFactory.load()

  private[this] val forIntegration = new PostgresTestConfig(
    conf.getString("db.postgres.url"),
    conf.getString("db.postgres.username"),
    conf.getString("db.postgres.password")
  )

  private[this] val forBenchMark = new PostgresTestConfig(
    conf.getString("db.postgres.url") + "?currentSchema=bench",
    conf.getString("db.postgres.username"),
    conf.getString("db.postgres.password")) {
    override def getBatchSize: Int = 1000
  }

  def get: PostgresTestConfig = forIntegration

  def getForBenchMark: PostgresTestConfig = forBenchMark

}
