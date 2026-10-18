import React, { useState, useEffect } from 'react';
import {
  Box,
  Heading,
  VStack,
  FormControl,
  FormLabel,
  Input,
  Button,
  Switch,
  Text,
  useColorModeValue,
  Alert,
  AlertIcon,
  HStack,
  Divider,
  Badge,
  Spinner,
} from '@chakra-ui/react';
import { FiCheck, FiX, FiSettings, FiKey } from 'react-icons/fi';
import { settingsService } from '../services/api';

const SettingsPage = () => {
  const [settings, setSettings] = useState({
    apiUrl: '',
    theme: 'light',
    notifications: true,
    autoSave: true,
  });
  const [connectionStatus, setConnectionStatus] = useState({
    mistral: null,
    tavily: null,
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const bgColor = useColorModeValue('white', 'gray.800');
  const borderColor = useColorModeValue('gray.200', 'gray.600');

  // Charger les paramètres au démarrage
  useEffect(() => {
    loadSettings();
  }, []);

  const loadSettings = async () => {
    setIsLoading(true);
    try {
      const response = await settingsService.getSettings();
      setSettings(response.data || settings);
    } catch (err) {
      console.error('Erreur lors du chargement des paramètres:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSettingChange = (e) => {
    const { name, value, type, checked } = e.target;
    setSettings((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSaveSettings = async () => {
    setIsLoading(true);
    setError(null);
    setSuccess(null);

    try {
      await settingsService.updateSettings(settings);
      setSuccess('Paramètres sauvegardés avec succès !');
    } catch (err) {
      console.error('Erreur lors de la sauvegarde des paramètres:', err);
      setError(err.message || "Erreur lors de la sauvegarde des paramètres");
    } finally {
      setIsLoading(false);
    }
  };

  const testMistralConnection = async () => {
    setConnectionStatus((prev) => ({ ...prev, mistral: 'testing' }));
    try {
      await settingsService.testMistralConnection();
      setConnectionStatus((prev) => ({ ...prev, mistral: 'success' }));
    } catch (err) {
      setConnectionStatus((prev) => ({ ...prev, mistral: 'error' }));
    }
  };

  const testTavilyConnection = async () => {
    setConnectionStatus((prev) => ({ ...prev, tavily: 'testing' }));
    try {
      await settingsService.testTavilyConnection();
      setConnectionStatus((prev) => ({ ...prev, tavily: 'success' }));
    } catch (err) {
      setConnectionStatus((prev) => ({ ...prev, tavily: 'error' }));
    }
  };

  const getConnectionStatusColor = (status) => {
    switch (status) {
      case 'success':
        return 'green';
      case 'error':
        return 'red';
      case 'testing':
        return 'blue';
      default:
        return 'gray';
    }
  };

  const getConnectionStatusText = (status) => {
    switch (status) {
      case 'success':
        return 'Connecté';
      case 'error':
        return 'Échec de connexion';
      case 'testing':
        return 'Test en cours...';
      default:
        return 'Non testé';
    }
  };

  return (
    <Box h="full" display="flex" flexDirection="column" bg={bgColor} borderRadius="lg" boxShadow="sm">
      {/* En-tête */}
      <Box p={4} borderBottom="1px" borderBottomColor={borderColor}>
        <HStack>
          <IconButton icon={<FiSettings />} variant="ghost" />
          <Heading as="h2" size="lg" color="gray.800">
            Paramètres
          </Heading>
        </HStack>
      </Box>

      {/* Contenu */}
      <Box flex={1} p={4} overflowY="auto">
        <VStack spacing={6} align="stretch">
          {/* Section API */}
          <Box>
            <Heading as="h3" size="md" mb={4}>
              <HStack>
                <FiKey />
                <Text>Configuration des APIs</Text>
              </HStack>
            </Heading>

            <VStack spacing={4} align="stretch" bg="gray.50" p={4} borderRadius="lg">
              {/* Test de connexion Mistral */}
              <HStack justify="space-between">
                <Text fontWeight="medium">Connexion à Mistral AI</Text>
                <HStack>
                  {connectionStatus.mistral === 'testing' ? (
                    <Spinner size="sm" />
                  ) : (
                    <Badge colorScheme={getConnectionStatusColor(connectionStatus.mistral)}>
                      {getConnectionStatusText(connectionStatus.mistral)}
                    </Badge>
                  )}
                  <Button
                    size="sm"
                    leftIcon={<FiCheck />}
                    onClick={testMistralConnection}
                    isLoading={connectionStatus.mistral === 'testing'}
                  >
                    Tester
                  </Button>
                </HStack>
              </HStack>

              <Divider />

              {/* Test de connexion Tavily */}
              <HStack justify="space-between">
                <Text fontWeight="medium">Connexion à Tavily</Text>
                <HStack>
                  {connectionStatus.tavily === 'testing' ? (
                    <Spinner size="sm" />
                  ) : (
                    <Badge colorScheme={getConnectionStatusColor(connectionStatus.tavily)}>
                      {getConnectionStatusText(connectionStatus.tavily)}
                    </Badge>
                  )}
                  <Button
                    size="sm"
                    leftIcon={<FiCheck />}
                    onClick={testTavilyConnection}
                    isLoading={connectionStatus.tavily === 'testing'}
                  >
                    Tester
                  </Button>
                </HStack>
              </HStack>
            </VStack>
          </Box>

          <Divider />

          {/* Section Préférences */}
          <Box>
            <Heading as="h3" size="md" mb={4}>
              Préférences
            </Heading>

            <VStack spacing={4} align="stretch" bg="gray.50" p={4} borderRadius="lg">
              <FormControl display="flex" alignItems="center">
                <FormLabel htmlFor="notifications" mb="0">
                  Notifications
                </FormLabel>
                <Switch
                  id="notifications"
                  name="notifications"
                  isChecked={settings.notifications}
                  onChange={handleSettingChange}
                />
              </FormControl>

              <FormControl display="flex" alignItems="center">
                <FormLabel htmlFor="autoSave" mb="0">
                  Sauvegarde automatique
                </FormLabel>
                <Switch
                  id="autoSave"
                  name="autoSave"
                  isChecked={settings.autoSave}
                  onChange={handleSettingChange}
                />
              </FormControl>

              <FormControl>
                <FormLabel>URL de l'API (optionnel)</FormLabel>
                <Input
                  name="apiUrl"
                  value={settings.apiUrl}
                  onChange={handleSettingChange}
                  placeholder="http://localhost:8081/api"
                />
              </FormControl>
            </VStack>
          </Box>

          {/* Affichage des messages de succès/erreur */}
          {success && (
            <Alert status="success" borderRadius="lg">
              <AlertIcon />
              {success}
            </Alert>
          )}

          {error && (
            <Alert status="error" borderRadius="lg">
              <AlertIcon />
              {error}
            </Alert>
          )}
        </VStack>
      </Box>

      {/* Pied de page avec bouton de sauvegarde */}
      <Box p={4} borderTop="1px" borderTopColor={borderColor}>
        <HStack justify="flex-end">
          <Button
            colorScheme="primary"
            onClick={handleSaveSettings}
            isLoading={isLoading}
            leftIcon={<FiSettings />}
          >
            Sauvegarder les paramètres
          </Button>
        </HStack>
      </Box>
    </Box>
  );
};

export default SettingsPage;
