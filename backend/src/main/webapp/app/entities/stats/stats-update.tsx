import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDevices } from 'app/shared/model/devices.model';
import { getEntities as getDevices } from 'app/entities/devices/devices.reducer';
import { getEntity, updateEntity, createEntity, reset } from './stats.reducer';
import { IStats } from 'app/shared/model/stats.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IStatsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StatsUpdate = (props: IStatsUpdateProps) => {
  const [devicesId, setDevicesId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { statsEntity, devices, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/stats');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getDevices();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.insertAt = convertDateTimeToServer(values.insertAt);

    if (errors.length === 0) {
      const entity = {
        ...statsEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartCossiolBackendApp.stats.home.createOrEditLabel">Create or edit a Stats</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : statsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="stats-id">ID</Label>
                  <AvInput id="stats-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="tempLabel" for="stats-temp">
                  Temp
                </Label>
                <AvField
                  id="stats-temp"
                  type="string"
                  className="form-control"
                  name="temp"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="soilLabel" for="stats-soil">
                  Soil
                </Label>
                <AvField
                  id="stats-soil"
                  type="string"
                  className="form-control"
                  name="soil"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="lightLabel" for="stats-light">
                  Light
                </Label>
                <AvField
                  id="stats-light"
                  type="string"
                  className="form-control"
                  name="light"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="insertAtLabel" for="stats-insertAt">
                  Insert At
                </Label>
                <AvInput
                  id="stats-insertAt"
                  type="datetime-local"
                  className="form-control"
                  name="insertAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.statsEntity.insertAt)}
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="stats-devices">Devices</Label>
                <AvInput id="stats-devices" type="select" className="form-control" name="devices.id">
                  <option value="" key="0" />
                  {devices
                    ? devices.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.title}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/stats" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  devices: storeState.devices.entities,
  statsEntity: storeState.stats.entity,
  loading: storeState.stats.loading,
  updating: storeState.stats.updating,
  updateSuccess: storeState.stats.updateSuccess
});

const mapDispatchToProps = {
  getDevices,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StatsUpdate);
