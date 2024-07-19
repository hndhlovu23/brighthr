import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFolder } from 'app/shared/model/folder.model';
import { getEntities as getFolders } from 'app/entities/folder/folder.reducer';
import { IFiletype } from 'app/shared/model/filetype.model';
import { getEntities as getFiletypes } from 'app/entities/filetype/filetype.reducer';
import { IFile } from 'app/shared/model/file.model';
import { getEntity, updateEntity, createEntity, reset } from './file.reducer';

export const FileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const folders = useAppSelector(state => state.folder.entities);
  const filetypes = useAppSelector(state => state.filetype.entities);
  const fileEntity = useAppSelector(state => state.file.entity);
  const loading = useAppSelector(state => state.file.loading);
  const updating = useAppSelector(state => state.file.updating);
  const updateSuccess = useAppSelector(state => state.file.updateSuccess);

  const handleClose = () => {
    navigate('/file' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFolders({}));
    dispatch(getFiletypes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...fileEntity,
      ...values,
      folder: folders.find(it => it.id.toString() === values.folder.toString()),
      filetype: filetypes.find(it => it.id.toString() === values.filetype.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          ...fileEntity,
          date: convertDateTimeFromServer(fileEntity.date),
          folder: fileEntity?.folder?.id,
          filetype: fileEntity?.filetype?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="brighthrApp.file.home.createOrEditLabel" data-cy="FileCreateUpdateHeading">
            Create or edit a File
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="file-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="file-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 2, message: 'This field is required to be at least 2 characters.' },
                  maxLength: { value: 160, message: 'This field cannot be longer than 160 characters.' },
                }}
              />
              <ValidatedField
                label="Added"
                id="file-added"
                name="added"
                data-cy="added"
                type="datetime-local"
                placeholder="YYYY-MM-DD"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="file-folder" name="folder" data-cy="folder" label="Folder" type="select">
                <option value="" key="0" />
                {folders
                  ? folders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="file-filetype" name="filetype" data-cy="filetype" label="Filetype" type="select">
                <option value="" key="0" />
                {filetypes
                  ? filetypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/file" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FileUpdate;
