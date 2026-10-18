import React, { useState, useEffect } from 'react';
import {
  Box,
  Heading,
  VStack,
  HStack,
  Input,
  Button,
  Select,
  Text,
  useDisclosure,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalCloseButton,
  ModalBody,
  ModalFooter,
  FormControl,
  FormLabel,
  Textarea,
  useColorModeValue,
  Alert,
  AlertIcon,
  Spinner,
  Flex,
  Badge,
} from '@chakra-ui/react';
import { FiSearch, FiPlus, FiUpload, FiDownload } from 'react-icons/fi';
import KnowledgeCard from '../components/KnowledgeCard';
import { knowledgeService } from '../services/api';

const KnowledgeBasePage = () => {
  const [documents, setDocuments] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [categories, setCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedDocument, setSelectedDocument] = useState(null);
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    category: '',
    tags: [],
  });

  const bgColor = useColorModeValue('white', 'gray.800');
  const borderColor = useColorModeValue('gray.200', 'gray.600');

  // Charger les documents et catégories au démarrage
  useEffect(() => {
    loadDocuments();
    loadCategories();
  }, []);

  // Charger les documents
  const loadDocuments = async (query = '', category = '') => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await knowledgeService.getAllDocuments();
      let filteredDocuments = response.data || [];

      // Filtrer par requête de recherche
      if (query) {
        const lowerQuery = query.toLowerCase();
        filteredDocuments = filteredDocuments.filter(
          (doc) =>
            doc.title.toLowerCase().includes(lowerQuery) ||
            doc.content.toLowerCase().includes(lowerQuery) ||
            doc.tags.some((tag) => tag.toLowerCase().includes(lowerQuery))
        );
      }

      // Filtrer par catégorie
      if (category) {
        filteredDocuments = filteredDocuments.filter(
          (doc) => doc.category === category
        );
      }

      setDocuments(filteredDocuments);
    } catch (err) {
      console.error('Erreur lors du chargement des documents:', err);
      setError(err.message || "Erreur lors du chargement des documents");
    } finally {
      setIsLoading(false);
    }
  };

  // Charger les catégories
  const loadCategories = async () => {
    try {
      const response = await knowledgeService.getCategories();
      setCategories(response.data || []);
    } catch (err) {
      console.error('Erreur lors du chargement des catégories:', err);
    }
  };

  // Gérer la recherche
  const handleSearch = () => {
    loadDocuments(searchQuery, selectedCategory);
  };

  // Ouvrir le modal pour ajouter/modifier un document
  const openDocumentModal = (document = null) => {
    if (document) {
      setFormData({
        title: document.title,
        content: document.content,
        category: document.category,
        tags: document.tags.join(', '),
      });
      setSelectedDocument(document);
    } else {
      setFormData({
        title: '',
        content: '',
        category: categories[0] || '',
        tags: '',
      });
      setSelectedDocument(null);
    }
    onOpen();
  };

  // Sauvegarder un document
  const handleSaveDocument = async () => {
    setIsLoading(true);
    setError(null);

    try {
      const documentData = {
        title: formData.title,
        content: formData.content,
        category: formData.category,
        tags: formData.tags.split(',').map((tag) => tag.trim()).filter((tag) => tag),
      };

      if (selectedDocument) {
        // Mise à jour
        await knowledgeService.updateDocument(selectedDocument.id, documentData);
      } else {
        // Création
        await knowledgeService.createDocument(documentData);
      }

      onClose();
      loadDocuments(searchQuery, selectedCategory);
    } catch (err) {
      console.error('Erreur lors de la sauvegarde du document:', err);
      setError(err.message || "Erreur lors de la sauvegarde du document");
    } finally {
      setIsLoading(false);
    }
  };

  // Supprimer un document
  const handleDeleteDocument = async (id) => {
    try {
      await knowledgeService.deleteDocument(id);
      loadDocuments(searchQuery, selectedCategory);
    } catch (err) {
      console.error('Erreur lors de la suppression du document:', err);
      setError(err.message || "Erreur lors de la suppression du document");
    }
  };

  // Gérer le changement de champ dans le formulaire
  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <Box h="full" display="flex" flexDirection="column" bg={bgColor} borderRadius="lg" boxShadow="sm">
      {/* En-tête */}
      <Box p={4} borderBottom="1px" borderBottomColor={borderColor}>
        <HStack justify="space-between" align="center">
          <Heading as="h2" size="lg" color="gray.800">
            Base de Connaissances
          </Heading>
          <Button
            leftIcon={<FiPlus />}
            colorScheme="primary"
            onClick={() => openDocumentModal()}
          >
            Ajouter un document
          </Button>
        </HStack>
      </Box>

      {/* Barre de recherche et filtres */}
      <Box p={4} borderBottom="1px" borderBottomColor={borderColor}>
        <HStack spacing={4}>
          <Input
            placeholder="Rechercher dans les documents..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            flex={1}
          />
          <Select
            placeholder="Toutes les catégories"
            value={selectedCategory}
            onChange={(e) => setSelectedCategory(e.target.value)}
          >
            {categories.map((category) => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </Select>
          <Button leftIcon={<FiSearch />} colorScheme="primary" onClick={handleSearch}>
            Rechercher
          </Button>
        </HStack>

        {/* Boutons d'import/export */}
        <Flex mt={4} gap={4}>
          <Button
            leftIcon={<FiUpload />}
            colorScheme="teal"
            variant="outline"
            onClick={() => document.getElementById('file-upload').click()}
          >
            Importer
          </Button>
          <input
            id="file-upload"
            type="file"
            accept=".json,.txt"
            style={{ display: 'none' }}
            onChange={async (e) => {
              const file = e.target.files[0];
              if (file) {
                try {
                  await knowledgeService.importDocuments(file);
                  loadDocuments();
                } catch (err) {
                  setError(err.message || "Erreur lors de l'import");
                }
              }
            }}
          />
          <Button
            leftIcon={<FiDownload />}
            colorScheme="teal"
            variant="outline"
            onClick={async () => {
              try {
                const response = await knowledgeService.exportDocuments();
                // Créer un lien de téléchargement
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', 'knowledge-base-export.json');
                document.body.appendChild(link);
                link.click();
                link.remove();
              } catch (err) {
                setError(err.message || "Erreur lors de l'export");
              }
            }}
          >
            Exporter
          </Button>
        </Flex>
      </Box>

      {/* Zone des documents */}
      <Box flex={1} p={4} overflowY="auto">
        {isLoading && documents.length === 0 ? (
          <Box display="flex" justifyContent="center" py={10}>
            <Spinner size="xl" color="primary.500" />
          </Box>
        ) : documents.length === 0 ? (
          <Box textAlign="center" py={10}>
            <Heading as="h3" size="md" color="gray.500" mb={4}>
              Aucune donnée dans la base de connaissances
            </Heading>
            <Text color="gray.400">
              Ajoutez des documents pour permettre à l'agent IA de répondre plus précisément.
            </Text>
          </Box>
        ) : (
          <VStack align="stretch" spacing={4}>
            {documents.map((document) => (
              <KnowledgeCard
                key={document.id}
                document={document}
                onDelete={handleDeleteDocument}
                onEdit={openDocumentModal}
              />
            ))}
          </VStack>
        )}

        {/* Affichage des erreurs */}
        {error && (
          <Alert status="error" borderRadius="lg" mt={4}>
            <AlertIcon />
            {error}
          </Alert>
        )}
      </Box>

      {/* Modal pour ajouter/modifier un document */}
      <Modal isOpen={isOpen} onClose={onClose} size="2xl">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>
            {selectedDocument ? 'Modifier le document' : 'Ajouter un document'}
          </ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <VStack spacing={4}>
              <FormControl isRequired>
                <FormLabel>Titre</FormLabel>
                <Input
                  name="title"
                  value={formData.title}
                  onChange={handleFormChange}
                  placeholder="Titre du document"
                />
              </FormControl>

              <FormControl isRequired>
                <FormLabel>Catégorie</FormLabel>
                <Select
                  name="category"
                  value={formData.category}
                  onChange={handleFormChange}
                  placeholder="Sélectionnez une catégorie"
                >
                  {categories.map((category) => (
                    <option key={category} value={category}>
                      {category}
                    </option>
                  ))}
                </Select>
              </FormControl>

              <FormControl isRequired>
                <FormLabel>Contenu</FormLabel>
                <Textarea
                  name="content"
                  value={formData.content}
                  onChange={handleFormChange}
                  placeholder="Contenu du document (supports Markdown)"
                  minH="200px"
                  fontFamily="monospace"
                />
              </FormControl>

              <FormControl>
                <FormLabel>Tags (séparés par des virgules)</FormLabel>
                <Input
                  name="tags"
                  value={formData.tags}
                  onChange={handleFormChange}
                  placeholder="IA, Machine Learning, Python"
                />
              </FormControl>
            </VStack>
          </ModalBody>

          <ModalFooter>
            <Button variant="ghost" onClick={onClose} mr={3}>
              Annuler
            </Button>
            <Button
              colorScheme="primary"
              onClick={handleSaveDocument}
              isLoading={isLoading}
            >
              {selectedDocument ? 'Mettre à jour' : 'Ajouter'}
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Box>
  );
};

export default KnowledgeBasePage;
