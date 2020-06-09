import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './stats.reducer';
import { IStats } from 'app/shared/model/stats.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStatsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StatsDetail = (props: IStatsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { statsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Stats [<b>{statsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="temp">Temp</span>
          </dt>
          <dd>{statsEntity.temp}</dd>
          <dt>
            <span id="soil">Soil</span>
          </dt>
          <dd>{statsEntity.soil}</dd>
          <dt>
            <span id="light">Light</span>
          </dt>
          <dd>{statsEntity.light}</dd>
          <dt>
            <span id="insertAt">Insert At</span>
          </dt>
          <dd>
            <TextFormat value={statsEntity.insertAt} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>Devices</dt>
          <dd>{statsEntity.devices ? statsEntity.devices.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/stats" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stats/${statsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ stats }: IRootState) => ({
  statsEntity: stats.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StatsDetail);
