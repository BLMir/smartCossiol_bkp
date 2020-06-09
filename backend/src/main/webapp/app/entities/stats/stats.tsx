import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './stats.reducer';
import { IStats } from 'app/shared/model/stats.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStatsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Stats = (props: IStatsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { statsList, match, loading } = props;
  return (
    <div>
      <h2 id="stats-heading">
        Stats
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Stats
        </Link>
      </h2>
      <div className="table-responsive">
        {statsList && statsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Temp</th>
                <th>Soil</th>
                <th>Light</th>
                <th>Insert At</th>
                <th>Devices</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {statsList.map((stats, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${stats.id}`} color="link" size="sm">
                      {stats.id}
                    </Button>
                  </td>
                  <td>{stats.temp}</td>
                  <td>{stats.soil}</td>
                  <td>{stats.light}</td>
                  <td>
                    <TextFormat type="date" value={stats.insertAt} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{stats.devices ? <Link to={`devices/${stats.devices.id}`}>{stats.devices.title}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${stats.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${stats.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${stats.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Stats found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ stats }: IRootState) => ({
  statsList: stats.entities,
  loading: stats.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Stats);
