import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './devices.reducer';
import { IDevices } from 'app/shared/model/devices.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDevicesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DevicesDetail = (props: IDevicesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { devicesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Devices [<b>{devicesEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{devicesEntity.title}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{devicesEntity.type}</dd>
          <dt>User</dt>
          <dd>{devicesEntity.user ? devicesEntity.user.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/devices" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/devices/${devicesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ devices }: IRootState) => ({
  devicesEntity: devices.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DevicesDetail);
